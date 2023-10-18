package com.jvr.common.contracts

interface ILogger : IGetTag {
    fun d(tag: String, message: String)
    fun d(message: String) {
        d(getTag(), message)
    }
    fun d(context: BaseActivityClass, message: String)

    fun i(tag: String, message: String)
    fun i(message: String) {
        i(getTag(), message)
    }
    fun i(context: BaseActivityClass, message: String)

    fun w(tag: String, message: String)
    fun w(message: String) {
        d(getTag(), message)
    }
    fun w(context: BaseActivityClass, message: String)

    fun e(context: BaseActivityClass, message: String)
    fun e(context: BaseActivityClass, message: Exception)
    fun e(tag: String, message: Exception)
    fun e(message: Exception) {
        e(getTag(),message)
    }
    fun e(message: String) {
        e(getTag(),java.lang.Exception(message))
    }
}