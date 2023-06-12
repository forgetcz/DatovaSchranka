package com.jvr.datovaschranka.dbhelper.tableModel

interface ITableItem<TInteger, TDate> {
  val id: TInteger?
  val dateCreated : TDate?
  val dateUpdated : TDate?
  val testItem : Boolean?
}