package com.jvr.datovaschranka.dbhelper.tableModel.v1

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.os.Parcelable
import com.jvr.datovaschranka.constatns.TimeUtils
import com.jvr.datovaschranka.dbhelper.tableModel.ITableItem
import com.jvr.datovaschranka.dbhelper.tableModel.BaseTable
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class UsersTable : BaseTable<UsersTable.Item>() {
    @Parcelize
    data class Item (
        override var _id: Int? = null,
        override var dateCreated : String? = null,
        override var dateUpdated : String? = null,
        override var testItem: Boolean? = null,
        var nickName: String? = null,
        var mark : String? = null
    ) : ITableItem<Int, String>, Parcelable {
        override fun toString(): String = "$COLUMN_ID : $_id; $COLUMN_NICK_NAME : $nickName"
        override fun insertAllowed(): Boolean {
            return _id == null && nickName != null
        }
    }

    companion object {
        const val COLUMN_ID = "_id"
        private const val COLUMN_DATE_CREATED = "dateCreated"
        const val COLUMN_DATE_UPDATED = "dateUpdated"
        const val COLUMN_TEST_ITEM = "testItem"
        const val COLUMN_NICK_NAME = "nickName"
        private const val COLUMN_MARK = "mark"
    }


    override fun getCreateModel(): String {
        return "CREATE TABLE " + getTableName() + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE" +
                "," + COLUMN_DATE_CREATED + " TEXT NOT NULL" +
                "," + COLUMN_DATE_UPDATED + " TEXT" +
                "," + COLUMN_NICK_NAME + " TEXT NOT NULL UNIQUE" +
                "," + COLUMN_MARK + " TEXT NULL" +
                "," + COLUMN_TEST_ITEM + " INTEGER NOT NULL" +
                ")"
    }

    override fun select(where: String?, limit : Int?): ArrayList<Item>? {
        val resultList = ArrayList<Item>()
        val cursor = getCursor(where, limit)

        if (cursor != null && cursor.moveToFirst()) {
            val iId = cursor.getColumnIndex(COLUMN_ID)
            val iDateCreated = cursor.getColumnIndex(COLUMN_DATE_CREATED)
            val iDateUpdated = cursor.getColumnIndex(COLUMN_DATE_UPDATED)
            val iMark = cursor.getColumnIndex(COLUMN_MARK)
            val iName = cursor.getColumnIndex(COLUMN_NICK_NAME)
            val iTestItem = cursor.getColumnIndex(COLUMN_TEST_ITEM)

            while (!cursor.isAfterLast) {
                val retItem = Item()
                retItem._id = cursor.getInt(iId)
                retItem.dateCreated = cursor.getString(iDateCreated)
                retItem.dateUpdated = cursor.getString(iDateUpdated)
                retItem.mark = cursor.getString(iMark)
                retItem.nickName = cursor.getString(iName)
                retItem.testItem = cursor.getInt(iTestItem) == 1

                resultList.add(retItem)
                cursor.moveToNext()

            }
            return resultList
        }
        return null
    }

    fun getMaxUserId() : Int? {
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT max($COLUMN_ID) + 1"
                    + " AS maxId FROM ${getTableName()}", null)
            cursor?.moveToFirst()
            val id = cursor.getInt(0)
            return id
        } catch (e: SQLiteException) {
            logger.e(getTag(),e)
            cursor?.close()
            return null
        }
    }

    override fun insert(item: Item): Boolean {
        // Create a new map of values, where column names are the keys
        logger.d(getTag(),"Insert called...")
        if (item._id != null) {
            logger.w(getTag(),"Element already created")
            return false
        }
        if (item.nickName == null) {
            logger.w(getTag(),"Element nickName can't be empty")
            return false
        }
        val values = ContentValues()

        val created = TimeUtils.currentDateTimeString(Date())
        values.put(COLUMN_DATE_CREATED, created)
        values.put(COLUMN_NICK_NAME, item.nickName.toString())
        values.put(COLUMN_MARK, item.mark.toString())

        if (item.testItem == null) {
            item.testItem = false
        }

        if (item.testItem == true) {
            values.put(COLUMN_TEST_ITEM, 1)
        } else
        {
            values.put(COLUMN_TEST_ITEM, 0)
        }

        // Insert the new row, returning the primary key value of the new row
        try {
            val newRowId = db.insert(getTableName(), null, values)

            if (newRowId == -1L) {
                logger.e(getTag(), java.lang.Exception("Error insert row"))
                return false
            }

            logger.d(getTag(),"Insert column $newRowId")
            item._id = Integer.parseInt(newRowId.toString())//getMaxUserId()
            item.dateCreated = created
            return newRowId != 0L
        } catch (e: Exception) {
            logger.e(getTag(), e)
            return false
        }
    }

    override fun update(item: Item): Boolean {
        val values = ContentValues()

        if (item._id == null) {
            logger.w(getTag(),"Element not yet exists!")
            return false
        }
        val dateUpdated = TimeUtils.currentDateTimeString(Date())
        values.put(COLUMN_DATE_UPDATED, dateUpdated)
        values.put(COLUMN_NICK_NAME, item.nickName)
        values.put(COLUMN_MARK, item.mark)
        if (item.testItem == null) {
            item.testItem = true
        }
        if (item.testItem == true) {
            values.put(COLUMN_TEST_ITEM, 1)
        } else {
            values.put(COLUMN_TEST_ITEM, 0)
        }

        val updated = db.update(getTableName(), values, "$COLUMN_ID = ?", arrayOf(item._id.toString()))
        logger.d(getTag(),"Updated column $updated")
        item.dateUpdated = dateUpdated
        return true
    }

    override fun delete(item: Item): Boolean {
        // Define 'where' part of query.
        val selection = "$COLUMN_ID LIKE ? "
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(item._id.toString())
        // Issue SQL statement.
        val deleteResult = db.delete(getTableName(), selection, selectionArgs)
        logger.d(getTag(),"Delete column $deleteResult")
        return deleteResult == 0
    }

    override fun insertDefaultTableData() {
        val item = UsersTable.Item(nickName = "Jiri Vrabec", testItem = true)
        insert(item)
    }
}