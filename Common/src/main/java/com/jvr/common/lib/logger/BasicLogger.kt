package com.jvr.common.lib.logger

import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger

class BasicLogger : ILogger {
    override fun d(context: BaseActivityClass, message: String) {
        d(context.getTag(), message)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun i(context: BaseActivityClass, message: String) {
        i(context.getTag(), message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun w(context: BaseActivityClass, message: String) {
        Log.w(context.getTag(), message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun e(context: BaseActivityClass, message: String) {
        Log.e(context.getTag(), message)
    }

    override fun e(context: BaseActivityClass, message: Exception) {
        Log.e(context.getTag(), message.message!!)
    }

    override fun e(tag: String, message: Exception) {
        Log.e(tag, message.message!!)
    }
}