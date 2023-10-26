package com.jvr.common.lib.logger

import android.content.Context
import com.jvr.common.contracts.ILogger
import android.os.Vibrator
import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import kotlin.Exception

class VibrateLogger : ILogger {
    private val logger: ComplexLogger = ComplexLogger(
        this.javaClass.name,mutableListOf(
            BasicLogger(), HistoryLogger()
        )
    )

    override fun d(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun d(tag: String, message: String) {
        Log.d(tag, ILogger.errMessageNotTargeted)
    }
    override fun d(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), ILogger.errMessageNotTargeted)
    }

    override fun i(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun i(tag: String, message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun i(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), ILogger.errMessageNotTargeted)
    }

    override fun w(message: String) {
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun w(tag: String, message: String) {
        Log.d(tag, ILogger.errMessageNotTargeted)

    }
    override fun w(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), ILogger.errMessageNotTargeted)
    }

    override fun e(message: String) {
        Log.d(getTag(), ILogger.errMessageNoContext)
    }
    override fun e(context: BaseActivityClass, message: String) {
        val pattern = longArrayOf(1500, 800, 800, 800)
        var vibrator: Vibrator? = null
        try {
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(pattern, 3)
            Thread.sleep(5000)
        } catch (ex: Exception) {
            logger.e(context, ex)
        } finally {
            vibrator!!.cancel()
        }
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        if (message.message != null) {
            e(context, message.message!!)
        } else {
            e(context, "Unknown message")
        }
    }
    override fun e(tag: String, message: Exception) {
        Log.d(tag, ILogger.errMessageNoContext)
    }
    override fun e(message: Exception) {
        Log.d(getTag(), ILogger.errMessageNoContext)
    }
}