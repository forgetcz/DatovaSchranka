package com.jvr.datovaschranka.dbhelper.tableModel.v1

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import com.jvr.datovaschranka.lib.classes.TimeUtils
import com.jvr.datovaschranka.dbhelper.tableModel.ITableItem
import com.jvr.datovaschranka.dbhelper.tableModel.BaseTable
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit


class TimeTable : BaseTable<TimeTable.Item>() {
    @Parcelize
    data class Item(
        override var _id : Int? = null,
        override var dateCreated : String? = null,
        override var dateUpdated : String? = null,
        override var testItem: Boolean = false,
        var fkUserId : Int? = null,
        var interval: Number? = null,
        var intervalUnit: TimeUnit? = null,/* NANOSECONDS,MICROSECONDS,MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS */
        var mark : String? = null
    ) : ITableItem<Int, String>, Parcelable {
        override fun toString(): String = "id:$_id; $COLUMN_INTERVAL_UNIT:$interval?,$COLUMN_INTERVAL_UNIT :$intervalUnit?"
        override fun insertAllowed(): Boolean {
            return _id == null && fkUserId != null && interval != null && intervalUnit != null
        }
    }

    companion object {

        private const val COLUMN_ID = "_id"
        private const val COLUMN_FK_USER_ID = "fkUserId"
        private const val COLUMN_DATE_CREATED = "dateCreated"
        private const val COLUMN_DATE_UPDATED = "dateUpdated"
        private const val COLUMN_TEST_ITEM = "testItem"
        private const val COLUMN_INTERVAL = "interval"
        private const val COLUMN_INTERVAL_UNIT = "intervalUnit"
        private const val COLUMN_MARK = "mark"
        private const val TEST_ITEM = "testItem"
    }

    override fun getCreateModel(): String {
        return "CREATE TABLE " +getTableName() + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE" +
                "," + COLUMN_FK_USER_ID + " INTEGER NOT NULL" +
                "," + COLUMN_DATE_CREATED + " TEXT NOT NULL" +
                "," + COLUMN_DATE_UPDATED + " TEXT" +
                "," + COLUMN_INTERVAL + " INTEGER NOT NULL" +
                "," + COLUMN_INTERVAL_UNIT + " INTEGER NOT NULL" +
                "," + COLUMN_MARK + " TEXT NULL UNIQUE" +
                "," + COLUMN_TEST_ITEM + " INTEGER NOT NULL" +
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

    /*
    * https://medium.com/@manojbhadane/kotlin-converting-between-integers-and-enum-values-in-kotlin-eecb80270a4*/
    /*private inline fun <reified T : Enum<T>> Int.toEnum(): T? {
        return enumValues<T>().firstOrNull { it.ordinal == this }
    }*/

    //Enum to Int
    /*private inline fun <reified T : Enum<T>> T.toInt(): Int {
        return this.ordinal
    }*/

    override fun select(where: String?, limit : Int?): ArrayList<Item>? {
        val resultList = ArrayList<Item>()
        val cursor: Cursor? = getCursor(where, limit)

        if (cursor != null && cursor.moveToFirst()) {
            val iId = cursor.getColumnIndex(COLUMN_ID)
            val iFkUserId = cursor.getColumnIndex(COLUMN_FK_USER_ID)
            val iDateCreated = cursor.getColumnIndex(COLUMN_DATE_CREATED)
            val iDateUpdated = cursor.getColumnIndex(COLUMN_DATE_UPDATED)
            val iInterval = cursor.getColumnIndex(COLUMN_INTERVAL)
            val iIntervalUnit = cursor.getColumnIndex(COLUMN_INTERVAL_UNIT)
            val iMark = cursor.getColumnIndex(COLUMN_MARK)
            val iTestItem = cursor.getColumnIndex(COLUMN_TEST_ITEM)

            while (!cursor.isAfterLast) {
                val retItem = Item()
                retItem._id = cursor.getInt(iId)
                retItem.fkUserId = cursor.getInt(iFkUserId)
                retItem.dateCreated = cursor.getString(iDateCreated)
                retItem.dateUpdated = cursor.getString(iDateUpdated)
                retItem.interval = cursor.getInt(iInterval)
                val intervalUnit = cursor.getInt(iIntervalUnit)
                retItem.intervalUnit = TimeUnit.values()[intervalUnit]
                retItem.mark = cursor.getString(iMark)
                retItem.testItem = cursor.getInt(iTestItem) == 1

                resultList.add(retItem)
                cursor.moveToNext();
                /*val valI = cursor.getInt(iIntervalUnit)
                println(valI)
                val myEnumValue: TimeUnit? = 1.toEnum<TimeUnit>()
                val theOne = enumValueOf<TimeUnit>("NANOSECONDS")
                //val theOne1 = TimeUnit.values().firstOrNull { it.name.equals("one", true) }
                val  value:TimeUnit = TimeUnit.values()[0]
                println(value)
                val x1 = TimeUnit.MINUTES.ordinal
                println(x1)

                println(theOne)
                if (myEnumValue != null) {
                    retItem.intervalUnit = myEnumValue
                }*/
            }

            return resultList
        }
        return null

    }

    override fun insert(item: Item): Boolean {
        // Create a new map of values, where column names are the keys
        if (item._id != null) {
            logger.w(getTag(),"Element already created")
            return false
        }
        if (!item.insertAllowed()) {
            logger.w(getTag(),"Not all required parameters is filled")
            return false
        }

        val values = ContentValues()

        val created = TimeUtils.currentDateTimeString(Date())
        values.put(COLUMN_DATE_CREATED, created)
        values.put(COLUMN_FK_USER_ID, item.fkUserId.toString())
        values.put(COLUMN_INTERVAL, item.interval.toString())
        values.put(COLUMN_INTERVAL_UNIT, item.intervalUnit?.ordinal)
        values.put(COLUMN_MARK, item.mark)

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

            item._id = Integer.parseInt(newRowId.toString())
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
        values.put(COLUMN_INTERVAL, item.interval.toString())
        values.put(COLUMN_INTERVAL_UNIT, item.intervalUnit?.ordinal)
        values.put(COLUMN_MARK, item.mark)
        if (item.testItem != null && item.testItem == true) {
            values.put(COLUMN_TEST_ITEM, 1)
        } else
        {
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
    }

}