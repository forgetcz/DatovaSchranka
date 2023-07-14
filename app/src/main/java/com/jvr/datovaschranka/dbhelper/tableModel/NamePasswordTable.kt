package com.jvr.datovaschranka.dbhelper.tableModel

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Parcelable
import com.jvr.datovaschranka.constatns.Utils
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

class NamePasswordTable: ModelTable<NamePasswordTable.Item>() {
    @Parcelize
    data class Item (
        override var id : Int? = null,
        override var dateCreated : String? = null,
        override var dateUpdated : String? = null,
        override var testItem: Boolean? = null,
        var fkUserId : Int? = null,
        var userName : String? = null,
        var userPassword : String? = null,
        var isActive : Boolean? = null
    ) : ITableItem<Int, String>, Parcelable {
        override fun toString(): String = "id:$id; userName:$userName?"
    }

    companion object {
        private const val TABLE_NAME = "NamePassword"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_FK_USER_ID = "fkUserId"
        private const val COLUMN_DATE_CREATED = "dateCreated"
        private const val COLUMN_DATE_UPDATED = "dateUpdated"
        private const val COLUMN_USER_NAME = "userName"
        private const val COLUMN_PASSWORD = "Password"
        private const val COLUMN_TEST_ITEM = "testItem"
        private const val COLUMN_IS_ACTIVE = "isActive"
    }

    override fun getTableName(): String {
        return TABLE_NAME
    }

    override fun getCreateModel(): String {
        return "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                "," + COLUMN_DATE_CREATED + " TEXT NOT NULL" +
                "," + COLUMN_DATE_UPDATED + " TEXT" +
                "," + COLUMN_TEST_ITEM + " INTEGER NOT NULL" +
                "," + COLUMN_FK_USER_ID + " INTEGER NOT NULL" +
                "," + COLUMN_USER_NAME + " TEXT NOT NULL" +
                "," + COLUMN_PASSWORD + " TEXT NOT NULL" +
                "," + COLUMN_IS_ACTIVE + " INTEGER NOT NULL" +
                ")"
    }

    /*override fun getColumnNames(): List<String> {
        val list = ArrayList<String>()
        val iterator = Item::class.java.declaredFields
        iterator.forEach {
            list.add(it.name)
        }
        return list
    }*/

    override fun select(where: String?, limit : Int?): ArrayList<Item>? {
        val resultList = ArrayList<Item>()
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

            val rawSql = "SELECT * FROM $TABLE_NAME $sqlWhere $sqlLimit"

            cursor = db.rawQuery(rawSql, null)
        } catch (e: SQLiteException) {
            cursor?.close()
            return null
        }

        if (cursor!!.moveToFirst()) {
            val iId = cursor.getColumnIndex(COLUMN_ID)
            val iDateCreated = cursor.getColumnIndex(COLUMN_DATE_CREATED)
            val iDateUpdated = cursor.getColumnIndex(COLUMN_DATE_UPDATED)
            val iTestItem = cursor.getColumnIndex(COLUMN_TEST_ITEM)
            val ifkUserId = cursor.getColumnIndex(COLUMN_FK_USER_ID)
            val iUserName = cursor.getColumnIndex(COLUMN_USER_NAME)
            val iUserPassword = cursor.getColumnIndex(COLUMN_PASSWORD)
            val iIsActive = cursor.getColumnIndex(COLUMN_IS_ACTIVE)

            if (!cursor.isAfterLast) {
                val retItem = Item()
                retItem.id = cursor.getInt(iId)
                retItem.dateCreated = cursor.getString(iDateCreated)
                retItem.dateUpdated = cursor.getString(iDateUpdated)
                retItem.testItem = cursor.getInt(iTestItem) == 1
                retItem.fkUserId = cursor.getInt(ifkUserId)
                retItem.userName = cursor.getString(iUserName)
                retItem.userPassword = cursor.getString(iUserPassword)
                retItem.isActive = cursor.getInt(iIsActive) == 1

                resultList.add(retItem)
                cursor.moveToNext();
            }
        }
        return resultList
    }

    override fun insert(item: Item): Boolean {
        // Create a new map of values, where column names are the keys
        logger.d(getTag(),"Insert called...")
        if (item.id != null) {
            logger.w(getTag(),"Element already created")
            return false
        }
        val values = ContentValues()

        val created = Utils().currentDateTimeString()

        if (item.testItem == null) {
            item.testItem = false
        }

        values.put(COLUMN_DATE_CREATED, created)

        if (item.testItem == true) {
            values.put(COLUMN_TEST_ITEM, 1)
        } else
        {
            values.put(COLUMN_TEST_ITEM, 0)
        }
        values.put(COLUMN_FK_USER_ID, item.fkUserId)
        values.put(COLUMN_USER_NAME, item.userName)
        values.put(COLUMN_PASSWORD, item.userPassword)

        if (item.isActive == null) {
            item.isActive = false
        }

        if (item.isActive == true) {
            values.put(COLUMN_IS_ACTIVE, 1)
        } else
        {
            values.put(COLUMN_IS_ACTIVE, 0)
        }

        // Insert the new row, returning the primary key value of the new row
        try {
            val newRowId = db.insert(TABLE_NAME, null, values)

            if (newRowId == -1L) {
                logger.e(getTag(), java.lang.Exception("Error insert row"))
                return false
            }

            logger.d(getTag(),"Insert column $newRowId")
            item.id = Integer.parseInt(newRowId.toString())//getMaxUserId()
            item.dateCreated = created
            return newRowId != 0L
        } catch (e: Exception) {
            logger.e(getTag(), e)
            return false
        }
    }

    override fun update(item: Item): Boolean {
        val values = ContentValues()

        if (item.id == null) {
            logger.w(getTag(),"Element not yet exists!")
            return false
        }
        val dateUpdated = Utils().currentDateTimeString()
        values.put(COLUMN_DATE_UPDATED, dateUpdated)
        if (item.testItem != null && item.testItem == true) {
            item.testItem = true
            values.put(COLUMN_TEST_ITEM, 1)
        } else
        {
            item.testItem = false
            values.put(COLUMN_TEST_ITEM, 0)
        }
        values.put(COLUMN_USER_NAME, item.userName)
        values.put(COLUMN_PASSWORD, item.userPassword)

        if (item.isActive == null) {
            item.isActive = false
        }
        if (item.isActive == true) {
            values.put(COLUMN_IS_ACTIVE, 1)
        } else
        {
            values.put(COLUMN_IS_ACTIVE, 0)
        }

        val updated = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(item.id.toString()))
        logger.d(getTag(),"Updated column $updated")
        item.dateUpdated = dateUpdated
        return true
    }

    override fun delete(item: Item): Boolean {
        // Define 'where' part of query.
        val selection = "$COLUMN_ID LIKE ? "
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(item.id.toString())
        // Issue SQL statement.
        val deleteResult = db.delete(TABLE_NAME, selection, selectionArgs)
        logger.d(getTag(),"Delete column $deleteResult")
        return deleteResult == 0
    }
}