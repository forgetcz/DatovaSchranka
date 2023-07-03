package com.jvr.common.contracts

interface ILogger : IGetTag{
    fun d(context: BaseActivityClass, message: String)
    fun d(tag: String, message: String)
    fun i(context: BaseActivityClass, message: String)
    fun i(tag: String, message: String)
    fun w(context: BaseActivityClass, message: String)
    fun w(tag: String, message: String)
    fun e(context: BaseActivityClass, message: String)
    fun e(context: BaseActivityClass, message: Exception)
    fun e(tag: String, message: Exception)
}