package com.jvr.common.lib.logger

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import kotlin.Exception

class RingToneLogger : ILogger {
    private val logger: ComplexLogger = ComplexLogger(
        this.javaClass.name,mutableListOf(
            BasicLogger(), HistoryLogger() //er()
            //, new RestApiLogger()
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
        Log.d(tag, ILogger.errMessageNotTargeted)
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
        Log.d(getTag(), ILogger.errMessageNotTargeted)
    }
    override fun e(context: BaseActivityClass, message: String) {
        var ringtone: Ringtone? = null
        try {
            val notification: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r: Ringtone = RingtoneManager.getRingtone(context, notification)
            r.play()
            ringtone = RingtoneManager.getRingtone(
                context, RingtoneManager.getActualDefaultRingtoneUri(
                    context, RingtoneManager.TYPE_ALARM
                )
            )
            ringtone.play()
            Thread.sleep(5000)
        } catch (ex: Exception) {
            logger.e(context, ex)
        } finally {
            ringtone?.stop()
        }
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        e(context, message.message!!)
    }
    override fun e(tag: String, message: Exception) {
        Log.d(tag, ILogger.errMessageNoContext)
    }
    override fun e(message: Exception) {
        Log.d(getTag(), ILogger.errMessageNoContext)
    }
}