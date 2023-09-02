package com.jvr.datovaschranka.dbhelper.tableModel.v1

import android.database.Cursor
import android.os.Parcelable
import com.jvr.datovaschranka.constatns.TimeUtils
import com.jvr.datovaschranka.dbhelper.tableModel.ITableItem
import com.jvr.datovaschranka.dbhelper.tableModel.BaseTable
import kotlinx.parcelize.Parcelize
import java.util.*

class AppSettingsTable : BaseTable<AppSettingsTable.Item>() {
    @Parcelize
    data class Item(
        override var _id : Int? = null,
        override var dateCreated : Date? = null,
        override var dateUpdated : Date? = null,
        override var testItem: Boolean? = null,
        var name : String = "",
        var value: String = "",
    ) : ITableItem<Int, Date>, Parcelable {
        override fun toString(): String = "id:$_id; $COLUMN_NAME:$name?,$COLUMN_VALUE :${value.substring(0,10)} $?"
        override fun insertAllowed(): Boolean {
            return _id == null &&  name.isNotEmpty()
        }
    }

    companion object {
        private const val COLUMN_ID = "_id"
        private const val COLUMN_DATE_CREATED = "dateCreated"
        private const val COLUMN_DATE_UPDATED = "dateUpdated"

        private const val COLUMN_NAME = "name"
        private const val COLUMN_VALUE = "value"
    }

    override fun getCreateModel(): String {
        return """ 
            CREATE TABLE "${getTableName()}" (
            	"$COLUMN_ID"	INTEGER NOT NULL UNIQUE,
            	"$COLUMN_DATE_CREATED"	TEXT NOT NULL,
            	"$COLUMN_DATE_UPDATED"	TEXT,
            	"$COLUMN_NAME"	TEXT UNIQUE,
            	"$COLUMN_VALUE"	TEXT,
            	PRIMARY KEY("$COLUMN_NAME")
            )
        """.trimIndent()
    }

    @Suppress("UnnecessaryVariable")
    override fun select(where: String?, limit : Int?): ArrayList<Item>? {
        val resultList = ArrayList<Item>()
        val cursor: Cursor? = getCursor(where, limit)

        if (cursor != null && cursor.moveToFirst()) {
            val iId = cursor.getColumnIndex(COLUMN_ID)
            val iDateCreated = cursor.getColumnIndex(COLUMN_DATE_CREATED)
            val iDateUpdated = cursor.getColumnIndex(COLUMN_DATE_UPDATED)
            val iName = cursor.getColumnIndex(COLUMN_NAME)
            val iValue = cursor.getColumnIndex(COLUMN_VALUE)

            while (!cursor.isAfterLast) {
                val retItem = Item()
                retItem._id = cursor.getInt(iId)
                retItem.dateCreated = TimeUtils.getDateFromString(cursor.getString(iDateCreated))
                val sDateUpdated = cursor.getString(iDateUpdated)
                retItem.dateUpdated = TimeUtils.getDateFromString(sDateUpdated)

                retItem.name = cursor.getString(iName)
                retItem.value = cursor.getString(iValue)

                resultList.add(retItem)
                cursor.moveToNext()
                return resultList
            }
        }
        return null
    }

    fun findDataBox2(): String {
        //val template = "Firstname: %s, Lastname: %s, Id: %s, Company: %s, Role: %s, Department: %s, Address: %s ...";
        //String.format(template, "","")
        //MessageFormat.format("There's an incorrect value \"{0}\" in column # {1}", x, y);

        val items = select("name = 'FindDataBox2'",1) as ArrayList<Item>
        val sFindDataBox2 : Item = items[0]
        var sResultValue = sFindDataBox2.value

        /*val values: MutableMap<String, String> = HashMap()
        values["dbType"] = "OVM"
        values["firmName"] = "Ministerstvo vnitra"
        Log.d("",sResultValue)*/
        //sResultValue = String.format(sResultValue, "OVM", "Ministerstvo vnitra")
        //val message = StringSubstitutor.replace(sResultValue, values, "{", "}")
        return sResultValue
    }

    override fun insert(item: Item): Boolean {
        return false
    }

    override fun update(item: Item): Boolean {
        return true
    }

    override fun delete(item: Item): Boolean {
        return true
    }

    override fun insertDefaultTableData() {
        TODO("Not yet implemented")
    }

}