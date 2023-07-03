package com.jvr.common.lib.logger

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import java.lang.Exception

class RingToneLogger : ILogger {
    override fun getTag(): String { return javaClass.name }

    private val logger: ComplexLogger = ComplexLogger(
        listOf(
            BasicLogger(), HistoryLogger() //er()
            //, new RestApiLogger()
        )
    )

    override fun d(context: BaseActivityClass, message: String) {}
    override fun d(tag: String, message: String) {}
    override fun i(context: BaseActivityClass, message: String) {}
    override fun i(tag: String, message: String) {}
    override fun w(context: BaseActivityClass, message: String) {}
    override fun w(tag: String, message: String) {}
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
    override fun e(tag: String, message: Exception) {}
}