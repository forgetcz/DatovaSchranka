package com.jvr.common.contracts

interface ILogger : IGetTag {
    companion object {
        const val errMessageNoContext = "This method doesn't not targeted for this kind of  messages without context"
        const val errMessageNotTargeted = "This method isn't targeted for this kind of messages"
    }

    fun d(message: String)
    fun d(tag: String, message: String)
    fun d(context: BaseActivityClass, message: String)

    fun i(message: String)
    fun i(tag: String, message: String)
    fun i(context: BaseActivityClass, message: String)

    fun w(message: String)
    fun w(tag: String, message: String)
    fun w(context: BaseActivityClass, message: String)

    fun e(message: String)
    fun e(tag: String, message: Exception)
    fun e(message: Exception)
    fun e(context: BaseActivityClass, message: String)
    fun e(context: BaseActivityClass, message: Exception)
}