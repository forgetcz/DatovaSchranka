package com.jvr.datovaschranka.dbhelper

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.*
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import com.jvr.common.lib.logger.BasicLogger
import com.jvr.common.lib.logger.ComplexLogger
import com.jvr.common.lib.logger.HistoryLogger
import com.jvr.datovaschranka.dbhelper.tableModel.ModelTable
import com.jvr.datovaschranka.dbhelper.tableModel.NamePasswordTable
import com.jvr.datovaschranka.dbhelper.tableModel.TimeTable
import com.jvr.datovaschranka.dbhelper.tableModel.UserTable
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

/**
 * https://www.geeksforgeeks.org/android-sqlite-database-in-kotlin/
 */
class DbHelper(context: Context, factory: CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    //: SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_NAME = "FeedReader.db"
        const val DATABASE_VERSION = 1
    }

    private fun getTag(): String {
        val className = javaClass.name
        val stack = Thread.currentThread().stackTrace
        val parentFunctionName = stack[3].methodName
        val tagResult ="$className:$parentFunctionName"
        return tagResult
    }

    private val userTable = UserTable()
    private val timeTable = TimeTable()
    private val namePasswordTable = NamePasswordTable()

    private val modelTables: List<ModelTable<*>> =
        listOf(userTable, timeTable, namePasswordTable)

    val getUserTable: UserTable
        get() {
            return userTable
        }

    val getTimeTable: TimeTable
        get() {
            return timeTable
        }

    val getNamePasswordTable: NamePasswordTable
        get() {
            return namePasswordTable
        }


    private val logger: ComplexLogger = ComplexLogger(
        listOf(
            BasicLogger(), HistoryLogger()
        )
    )

    init {
        modelTables.forEach { fe -> fe.setDatabase(writableDatabase) }
        modelTables.forEach { fe -> fe.setContext(context) }
        //val store = DbPreferences(context)
        //val tokenText = store.getAccessToken.collect(initial = "")
    }

    override fun onCreate(db: SQLiteDatabase) {
        modelTables.forEach { fe -> fe.onCreateTable(db) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL("DROP TABLE IF EXISTS " + NamePasswordModel.TABLE_NAME)
        modelTables.forEach { fe -> fe.onUpgradeTable(db, oldVersion, newVersion) }

        logger.i(getTag(), "onUpgrade:old:$oldVersion,new:$newVersion")
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}