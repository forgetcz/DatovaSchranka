package com.jvr.common.contracts

interface ILogger : IGetTag{
    fun d(context: BaseActivityClass, message: String)
    fun d(Tag: String, message: String)
    fun i(context: BaseActivityClass, message: String)
    fun i(Tag: String, message: String)
    fun w(context: BaseActivityClass, message: String)
    fun w(Tag: String, message: String)
    fun e(context: BaseActivityClass, message: String)
    fun e(context: BaseActivityClass, message: Exception)
    fun e(Tag: String, message: Exception)
}