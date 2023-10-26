package com.jvr.common.lib.logger

import android.util.Log
import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import com.jvr.common.contracts.ILogger.Companion.errMessageNoContext
import com.jvr.common.contracts.ILogger.Companion.errMessageNotTargeted
import com.jvr.common.lib.gmail.GMailSender
import kotlin.Exception

class GMailLogger(User: String, Pass: String, Smtp: String) : ILogger {

    private var gMailUser: String = User
    private var gMailPass: String = Pass
    private var gMailSmtp: String = Smtp

    private fun sendEmail(subject: String, messageBody: String) {
        GMailSender(gMailUser, gMailPass, gMailSmtp)
            .sendMail(
                "AndroidTerminal: $subject", messageBody, "vrabec@onio.cz", "vrabec@onio.cz"
            )
    }

    override fun d(message: String) {
        Log.d(getTag(),errMessageNotTargeted)
    }
    override fun d(tag: String, message: String) {
        Log.d(tag, errMessageNotTargeted)
    }
    override fun d(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(), errMessageNotTargeted)
    }

    override fun i(message: String) {
        Log.d(getTag(),errMessageNotTargeted)
    }
    override fun i(tag: String, message: String) {
        Log.d(tag,errMessageNotTargeted)
    }
    override fun i(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(),errMessageNotTargeted)
    }

    override fun w(message: String) {
        Log.d(getTag(), errMessageNotTargeted)
    }
    override fun w(tag: String, message: String) {
        Log.d(tag,errMessageNotTargeted)
    }
    override fun w(context: BaseActivityClass, message: String) {
        Log.d(context.getTag(),errMessageNotTargeted)
    }

    override fun e(message: String) {
        Log.d(getTag(), errMessageNoContext)
    }
    override fun e(context: BaseActivityClass, message: String) {
        sendEmail(context.getTag(), message)
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        sendEmail(context.getTag(), message.message!!)
    }
    override fun e(tag: String, message: Exception) {
        Log.d(tag, errMessageNoContext)
    }
    override fun e(message: Exception) {
        Log.d(getTag(), errMessageNoContext)
    }
}