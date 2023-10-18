package com.jvr.common.lib.logger

import com.jvr.common.contracts.BaseActivityClass
import com.jvr.common.contracts.ILogger
import com.jvr.common.lib.gmail.GMailSender
import java.lang.Exception

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

    override fun d(context: BaseActivityClass, message: String) {}
    override fun d(tag: String, message: String) {}
    override fun i(context: BaseActivityClass, message: String) {}
    override fun i(tag: String, message: String) {}
    override fun w(context: BaseActivityClass, message: String) {}
    override fun w(tag: String, message: String) {}
    override fun e(context: BaseActivityClass, message: String) {
        sendEmail(context.getTag(), message)
    }
    override fun e(context: BaseActivityClass, message: Exception) {
        sendEmail(context.getTag(), message.message!!)
    }
    override fun e(tag: String, message: Exception) {}
}