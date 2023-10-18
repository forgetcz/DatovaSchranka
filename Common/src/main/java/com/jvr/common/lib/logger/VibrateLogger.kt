package com.jvr.common.lib.logger

import android.content.Context
import com.jvr.common.contracts.ILogger
import android.os.Vibrator
import com.jvr.common.contracts.BaseActivityClass
import java.lang.Exception

class VibrateLogger : ILogger {
    private val logger: ComplexLogger = ComplexLogger(
        mutableListOf(
            BasicLogger(), HistoryLogger()
        )
    )

    override fun d(context: BaseActivityClass, message: String) {}
    override fun d(tag: String, message: String) {}
    override fun i(context: BaseActivityClass, message: String) {}
    override fun i(tag: String, message: String) {}
    override fun w(context: BaseActivityClass, message: String) {}
    override fun w(tag: String, message: String) {}
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
        e(context, message.message!!)
    }
    override fun e(tag: String, message: Exception) {}
}