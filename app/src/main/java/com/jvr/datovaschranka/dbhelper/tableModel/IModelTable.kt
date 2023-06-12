package com.jvr.datovaschranka.dbhelper.tableModel

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

interface IModelTable <TItemType> where TItemType : ITableItem<*,*> {
    fun getTableName() : String
    fun getCreateModel() : String

    fun setContext(context: Context)
    fun setDatabase(db: SQLiteDatabase)
    fun onCreateTable(db: SQLiteDatabase)
    fun onUpgradeTable(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun select(where : String?, limit : Int? = 1): ArrayList<TItemType>?
    fun insert(item : TItemType): Boolean
    fun update(item : TItemType) : Boolean
    fun delete(item : TItemType): Boolean
    fun delete(idsList : Array<String>): Boolean
    fun selectAll() : ArrayList<TItemType>?
    fun deleteAll() : Boolean

}