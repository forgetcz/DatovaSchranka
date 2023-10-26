package com.jvr.common.lib.logger

import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger

class BasicLogger : ILogger {
    override fun d(message: String) {
        Log.d(getTag(), message)
    }
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
    override fun d(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), message)
    }

    override fun i(message: String) {
        Log.i(getTag(), message)
    }
    override fun i(tag: String, message: String) {
        Log.i(getTag(), message)
    }
    override fun i(context: BaseActivityClass, message: String) {
        Log.i(context.getTag(), message)
    }

    override fun w(message: String) {
        Log.w(getTag(), message)
    }
    override fun w(tag: String, message: String) {
        Log.w(getTag(), message)
    }
    override fun w(context: BaseActivityClass, message: String) {
        Log.w(context.getTag(), message)
    }

    override fun e(message: String) {
        Log.e(getTag(), message)
    }
    override fun e(tag: String, message: Exception) {
        if (message.message != null) {
            Log.e(tag, message.message!!)
        } else {
            Log.e(tag, "Unknown message")
        }
    }
    override fun e(message: Exception) {
        e(getTag(), message)
    }
    override fun e(context: BaseActivityClass, message: String) {
        Log.e(context.getTag(), message)
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        e(context.getTag(), message)
    }

}