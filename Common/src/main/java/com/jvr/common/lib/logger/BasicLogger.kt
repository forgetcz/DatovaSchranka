package com.jvr.common.lib.logger

import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger

class BasicLogger : ILogger {
    override fun getTag(): String { return javaClass.name }
    //private val unknownSourceTag = "Unknown source"

    override fun d(context: BaseActivityClass, message: String) {
        d(context.getTag(), message)
    }

    override fun d(Tag: String, message: String) {
        Log.d(Tag, message)
    }

    override fun i(context: BaseActivityClass, message: String) {
        i(context.getTag(), message)
    }

    override fun i(Tag: String, message: String) {
        Log.i(Tag, message)
    }

    override fun w(context: BaseActivityClass, message: String) {
        Log.w(context.getTag(), message)
    }

    override fun w(Tag: String, message: String) {
        Log.w(Tag, message)
    }

    override fun e(context: BaseActivityClass, message: String) {
        Log.e(context.getTag(), message)
    }

    override fun e(context: BaseActivityClass, message: Exception) {
        Log.e(context.getTag(), message.message!!)
    }

    override fun e(Tag: String, message: Exception) {
        Log.e(Tag, message.message!!)
    }
}