package com.jvr.datovaschranka.dbhelper.tableModel

interface ITableItem<TInteger, TDate> {
  val _id: TInteger?
  val dateCreated : TDate?
  val dateUpdated : TDate?
  val testItem : Boolean?
}