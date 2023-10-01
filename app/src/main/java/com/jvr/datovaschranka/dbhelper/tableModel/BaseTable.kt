package com.jvr.datovaschranka.dbhelper.tableModel

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.dbhelper.tableModel.v1.NamePasswordTable
import java.util.ArrayList

abstract class BaseTable<TItemType> : ITable<TItemType> where TItemType : Any, TItemType : ITableItem<*,*> {
    protected lateinit var db: SQLiteDatabase
    lateinit var appContext: Context

    override fun setContextToChildrenTables(context: Context) {
        appContext = context
    }

    /**
     * Return full class & method name for logging
     */
    @Suppress("UnnecessaryVariable")
    protected fun getTag(): String {
        val className = javaClass.name
        val stack = Thread.currentThread().stackTrace
        val parentFunctionName = stack[3].methodName
        val tagResult ="$className:$parentFunctionName"
        return tagResult
    }

    protected val logger: ComplexLogger = ComplexLogger(
        listOf(
            BasicLogger(), HistoryLogger()
        )
    )

    fun getCursor(where: String?, limit: Int?): Cursor? {
        var cursor: Cursor? = null
        try {
            var sqlWhere = ""
            if ((where != null) && where.isNotEmpty()){
                if (!where.startsWith("WHERE")){
                    sqlWhere = "WHERE $where"
                } else {
                    sqlWhere = where
                }
            }

            var sqlLimit = ""
            if (limit != null) {
                sqlLimit = "LIMIT $limit"
            }

            val rawSql = "SELECT * FROM ${getTableName()} $sqlWhere $sqlLimit"

            cursor = db.rawQuery(rawSql, null)
            return cursor
        } catch (e: SQLiteException) {
            logger.e(getTag(), e)
            cursor?.close()
            return null
        }
    }

    @Suppress("UnnecessaryVariable")
    override fun getTableName(): String {
        val name = javaClass.simpleName.replace("Table","")
        return name
    }

    override fun onCreateTable(db: SQLiteDatabase) {
        try {
            val createTableScript = getCreateModel()
            db.execSQL(createTableScript)
            insertDefaultTableData()
            //val s1 = this::onCreateElement.name
            //val s2 = ::onCreateElement.name
            //val s3 = (object{}.javaClass.enclosingMethod?.name ?: "")
            //val s4 = Thread.currentThread().stackTrace[1].methodName
            //logger.d("${getTag()}:${::onCreateElement.name}", "${::onCreateElement.name}- OK")
        } catch (error : Exception){
            logger.e("${getTag()}:${::onCreateTable.name}", error)
        }
    }

    override fun onUpgradeTable(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            val tableName = getTableName()
            db.execSQL("DROP TABLE IF EXISTS $tableName")
            onCreateTable(db)
            //val s1 = this::onUpgradeElement.name
            //val s2 = ::onUpgradeElement.name
            //val s3 = (object{}.javaClass.enclosingMethod?.name ?: "")
            //val s4 = Thread.currentThread().stackTrace[1].methodName
            //logger.d(getTag(), "${::onUpgradeElement.name}- OK")
        } catch (error : Exception) {
            logger.e(getTag(), error)
        }
    }

    override fun setDatabaseToChildrenTables(db: SQLiteDatabase) {
        this.db = db
    }

    override fun deleteAll() : Boolean {
        try {
            db.execSQL("DELETE FROM ${getTableName()}")
            return true
        } catch (error: Exception) {
            logger.e(getTag(), error)
            return false
        }
    }

    override fun delete(item: TItemType): Boolean {
        // Define 'where' part of query.
        val selection = "_id LIKE ? "
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(item._id.toString())
        // Issue SQL statement.
        val deleteResult = db.delete(getTableName(), selection, selectionArgs)
        logger.d(getTag(),"Delete column $deleteResult")
        return deleteResult == 0
    }

    override fun delete(idsList : Array<String>): Boolean {
        // Define 'where' part of query.
        val selection = "_id LIKE ? "
        // Specify arguments in placeholder order.
        // val selectionArgs = arrayOf(idsList.toString())
        // Issue SQL statement.
        val deleteResult = db.delete(getTableName(), selection, idsList)
        logger.d(getTag(),"Delete column $deleteResult")
        return deleteResult == 0
    }

    override fun selectAll(): ArrayList<TItemType>? {
        return select(null, null)
    }
}