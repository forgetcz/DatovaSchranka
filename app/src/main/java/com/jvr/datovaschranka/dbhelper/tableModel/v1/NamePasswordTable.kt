package com.jvr.datovaschranka.dbhelper.tableModel.v1

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import com.jvr.common.lib.crypto.Rsa
import com.jvr.datovaschranka.lib.classes.TimeUtils
import com.jvr.datovaschranka.dbhelper.tableModel.ITableItem
import com.jvr.datovaschranka.dbhelper.tableModel.BaseTable
import kotlinx.parcelize.Parcelize
import java.util.*

class NamePasswordTable: BaseTable<NamePasswordTable.Item>() {
    @Parcelize
    data class Item (
        override var _id : Int? = null,
        override var dateCreated : String? = null,
        override var dateUpdated : String? = null,
        var fkUserId : Int? = null,
        var userName : String = "",
        var userPassword : String = "",
        var isActive : Boolean? = null
    ) : ITableItem<Int, String>, Parcelable {
        override fun toString(): String = "$COLUMN_ID:$_id; $COLUMN_USER_NAME:$userName?"
        override fun insertAllowed(): Boolean {
            return _id == null && fkUserId != null && isActive != null
        }
    }

    companion object {
        const val COLUMN_FK_USER_ID = "fkUserId"
        private const val COLUMN_USER_NAME = "userName"
        private const val COLUMN_PASSWORD = "Password"
        private const val COLUMN_IS_ACTIVE = "isActive"
    }

    override fun getCreateModel(): String {
        return "CREATE TABLE " + getTableName() + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE" +
                "," + COLUMN_DATE_CREATED + " TEXT NOT NULL" +
                "," + COLUMN_DATE_UPDATED + " TEXT" +
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
        val cursor: Cursor? = getCursor(where, limit)

        val rsa = Rsa(appContext)

        if (cursor != null && cursor.moveToFirst()) {
            val iId = cursor.getColumnIndex(COLUMN_ID)
            val iDateCreated = cursor.getColumnIndex(COLUMN_DATE_CREATED)
            val iDateUpdated = cursor.getColumnIndex(COLUMN_DATE_UPDATED)
            val ifkUserId = cursor.getColumnIndex(COLUMN_FK_USER_ID)
            val iUserName = cursor.getColumnIndex(COLUMN_USER_NAME)
            val iUserPassword = cursor.getColumnIndex(COLUMN_PASSWORD)
            val iIsActive = cursor.getColumnIndex(COLUMN_IS_ACTIVE)

            if (!cursor.isAfterLast) {
                val retItem = Item()
                retItem._id = cursor.getInt(iId)
                retItem.dateCreated = cursor.getString(iDateCreated)
                retItem.dateUpdated = cursor.getString(iDateUpdated)
                retItem.fkUserId = cursor.getInt(ifkUserId)
                retItem.userName = rsa.decCrypt(cursor.getString(iUserName))
                retItem.userPassword = rsa.decCrypt(cursor.getString(iUserPassword))
                retItem.isActive = cursor.getInt(iIsActive) == 1

                resultList.add(retItem)
                cursor.moveToNext()
            }
            return resultList
        }
        return null
    }

    override fun insert(item: Item): Boolean {
        // Create a new map of values, where column names are the keys
        logger.d(getTag(),"Insert called...")
        if (item._id != null) {
            logger.w(getTag(),"Element already created")
            return false
        }
        val values = ContentValues()

        val created = TimeUtils.currentDateTimeString(Date())

        values.put(COLUMN_DATE_CREATED, created)

        val rsa = Rsa(appContext)
        values.put(COLUMN_FK_USER_ID, item.fkUserId)
        values.put(COLUMN_USER_NAME, rsa.crypt(item.userName))
        values.put(COLUMN_PASSWORD, rsa.crypt(item.userPassword))

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

        val rsa = Rsa(appContext)
        values.put(COLUMN_USER_NAME, rsa.crypt(item.userName))
        values.put(COLUMN_PASSWORD, rsa.crypt(item.userPassword))

        if (item.isActive == null) {
            item.isActive = false
        }
        if (item.isActive == true) {
            values.put(COLUMN_IS_ACTIVE, 1)
        } else
        {
            values.put(COLUMN_IS_ACTIVE, 0)
        }

        val updated = db.update(getTableName(), values, "$COLUMN_ID = ?", arrayOf(item._id.toString()))
        logger.d(getTag(),"Updated column $updated")
        item.dateUpdated = dateUpdated
        return true
    }

    /*override fun delete(item: Item): Boolean {
        // Define 'where' part of query.
        val selection = "$COLUMN_ID LIKE ? "
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(item._id.toString())
        // Issue SQL statement.
        val deleteResult = db.delete(getTableName(), selection, selectionArgs)
        logger.d(getTag(),"Delete column $deleteResult")
        return deleteResult == 0
    }*/

    override fun insertDefaultTableData() {
        val item1 = Item(fkUserId = 1, userName = "h63c6h"
            , userPassword = "5CPOMFtsrX8yfejMnKlO9A", isActive = true)
        insert(item1)

        val item2 = Item(fkUserId = 2, userName = "45tej9"
            , userPassword = "gudYaNRz3E8Yx3xs4UXMyB", isActive = true)
        insert(item2)
    }
}