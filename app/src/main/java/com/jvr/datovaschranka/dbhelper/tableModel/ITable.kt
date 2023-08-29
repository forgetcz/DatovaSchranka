package com.jvr.datovaschranka.dbhelper.tableModel

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

interface ITable <TItemType> where TItemType : Any, TItemType : ITableItem<*,*> {
    fun getTableName() : String
    fun getCreateModel() : String

    fun setContextToChildrenTables(context: Context)
    fun setDatabaseToChildrenTables(db: SQLiteDatabase)

    // vola se pri create database
    fun onCreateTable(db: SQLiteDatabase)
    // vola se pri create database
    fun onUpgradeTable(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    fun select(where : String?, limit : Int? = 1): ArrayList<TItemType>?
    fun insert(item : TItemType): Boolean
    fun update(item : TItemType) : Boolean
    fun delete(item : TItemType): Boolean
    fun delete(idsList : Array<String>): Boolean
    fun selectAll() : ArrayList<TItemType>?
    fun deleteAll() : Boolean

}