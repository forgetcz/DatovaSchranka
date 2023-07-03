package com.jvr.common.lib.logger

import android.os.AsyncTask
import android.util.Log
import com.jvr.common.RunCommandAsync
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.Exception

class ComplexLogger(private var appenderList: List<ILogger>) : ILogger {
    override fun getTag(): String { return javaClass.name }

    override fun d(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            GlobalScope.async {
                try {
                    logger.d(context, message)
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                }
            }
        }
    }
    override fun d(tag: String, message: String) {
        for (logger in appenderList) {

            RunCommandAsync(null, "", label@{
                try {
                    logger.d(tag, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun i(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsync(context, "", label@{
                try {
                    logger.i(context, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun i(tag: String, message: String) {
        for (logger in appenderList) {
            RunCommandAsync(null, "", label@{
                try {
                    logger.i(tag, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun w(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsync(context, "", label@{
                try {
                    logger.w(context, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun w(tag: String, message: String) {
        for (logger in appenderList) {
            RunCommandAsync(null, "", label@{
                try {
                    logger.w(tag, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun e(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsync(context, "", label@{
                try {
                    logger.e(context, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        for (logger in appenderList) {
            RunCommandAsync(context, "", label@{
                try {
                    logger.e(context, message.message!!)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun e(tag: String, message: Exception) {
        for (logger in appenderList) {
            RunCommandAsync(null, "", label@{
                try {
                    logger.e(tag, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
}