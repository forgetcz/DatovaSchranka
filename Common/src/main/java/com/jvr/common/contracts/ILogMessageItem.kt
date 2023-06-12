package com.jvr.common.contracts

interface ILogMessageItem : IGetTag {
    fun getDateInsert(): String
    fun getMessage(): String
    fun getMethodName(): String
    fun getClassName(): String
    fun getFileName(): String
}