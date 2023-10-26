package com.jvr.common.lib.logger

import android.os.AsyncTask
import android.util.Log
import com.jvr.common.BuildConfig
import com.jvr.common.lib.async.RunCommandAsyncJava
import com.jvr.common.lib.async.RunCommandAsyncKotlin
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ComplexLogger(private val parentClassName : String,
                    private val appenderList: MutableList<ILogger>) : ILogger {

    override fun d(message: String) {
        d(parentClassName, message)
    }
    override fun d(tag: String, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncKotlin<Unit, Any, Int>(null, null
                , {
                    try {
                        logger.d(tag, message)
                        return@RunCommandAsyncKotlin
                    } catch (ex: Exception) {
                        Log.e(logger.getTag(), ex.message!!)
                    }
                }, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            /*RunCommandAsyncJava(null, "", label@{
                try {
                    logger.d(tag, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)*/
        }
    }
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

    override fun i(message: String) {
        i(parentClassName, message)
    }
    override fun i(tag: String, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncJava(null, "", {
                try {
                    logger.i(tag, message)
                    //true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    //false
                }
            }) { }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun i(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncJava(context, "", label@{
                try {
                    logger.i(context, message)
                    //return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    //return@label false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    override fun w(message: String) {
        w(parentClassName, message)
    }
    override fun w(tag: String, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncJava(null, "", {
                try {
                    logger.w(tag, message)
                    true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    false
                }
            }) { }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun w(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncJava(context, "", {
                try {
                    logger.w(context, message)
                    //true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    //false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    override fun e(message: String) {
        e(parentClassName, Exception(message))
    }
    override fun e(message: Exception) {
        e(parentClassName, message)
    }

    override fun e(tag: String, message: Exception) {
        for (logger in appenderList) {
            RunCommandAsyncJava(null, "", label@{
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
    override fun e(context: BaseActivityClass, message: String) {
        for (logger in appenderList) {
            RunCommandAsyncJava(context, "", label@{
                try {
                    logger.e(context, message)
                    return@label true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    return@label false
                }
            }, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        for (logger in appenderList) {
            RunCommandAsyncJava(context, "", {
                try {
                    logger.e(context, message.message!!)
                    true
                } catch (ex: Exception) {
                    Log.e(logger.getTag(), ex.message!!)
                    false
                }
            }) { res -> }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    /*init {
        if (BuildConfig.DEBUG) {
            appenderList.add(RestLogger("https://api.onio.cz/log-api/log-message"))
        }
    }*/
}