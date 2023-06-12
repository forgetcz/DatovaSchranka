package com.jvr.common.lib.gmail

import java.io.*
import java.lang.Exception
import java.security.Security
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/**
 * http://androidautomail.blogspot.com/2013/06/auto-generated-mail-from-android.html?m=1
 * https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
 */
class GMailSender(user: String, password: String, mailHost: String) : Authenticator() {
    /*var complexLogger: ComplexLogger = ComplexLogger(
        Arrays.asList(
            BasicLogger(), HistoryLogger() //, new GMailLogger()
            , RestApiLogger()
        )
    )*/
    private val gmailUser: String = user
    private val gmailPassword: String = password
    private var gmailSession: Session? = null

    companion object {
        init {
            Security.addProvider(JSSEProvider())
        }
    }

    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(gmailUser, gmailPassword)
    }

    @Synchronized
    fun sendMail(subject: String?, body: String, sender: String?, recipients: String) {
        try {
            val message = MimeMessage(gmailSession)
            val handler = DataHandler(ByteArrayDataSource(body.toByteArray(), "text/plain"))
            message.sender = InternetAddress(sender)
            message.subject = subject
            message.dataHandler = handler
            if (recipients.indexOf(',') > 0) message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipients)
            ) else message.setRecipient(
                Message.RecipientType.TO, InternetAddress(recipients)
            )
            Transport.send(message)
            //complexLogger.d("GmailSender", "sendMail:SEND")
        } catch (e: Exception) {
            //complexLogger.e(this.javaClass.name, e)
        }
    }

    @Synchronized
    @Throws(Exception::class)
    fun sendMail(
        subject: String?, body: String?, sender: String?, recipients: String, attachment: File?
    ) {
        try {
            val message = MimeMessage(gmailSession)
            message.sender = InternetAddress(sender)
            message.subject = subject
            val mbp1 = MimeBodyPart()
            mbp1.setText(body)
            val mbp2 = MimeBodyPart()
            val fds = FileDataSource(attachment)
            mbp2.dataHandler = DataHandler(fds)
            mbp2.fileName = fds.name
            val mp: Multipart = MimeMultipart()
            mp.addBodyPart(mbp1)
            mp.addBodyPart(mbp2)
            message.setContent(mp)
            if (recipients.indexOf(',') > 0) message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipients)
            ) else message.setRecipient(
                Message.RecipientType.TO, InternetAddress(recipients)
            )
            Transport.send(message)
        } catch (e: Exception) {
            //complexLogger.e(this.javaClass.name, e)
            e.printStackTrace()
        }
    }

    inner class ByteArrayDataSource : DataSource {
        private var byteData: ByteArray? = null
        private var byteType: String = ""

        constructor(data: ByteArray, type: String) : super() {
            byteData = data
            byteType = type
        }

        constructor(data: ByteArray) : super() {
            byteData = data
        }

        fun setType(type: String) {
            byteType = type
        }

        override fun getContentType(): String {
            return byteType
        }

        @Throws(IOException::class)
        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(byteData)
        }

        override fun getName(): String {
            return "ByteArrayDataSource"
        }

        @Throws(IOException::class)
        override fun getOutputStream(): OutputStream {
            throw IOException("Not Supported")
        }
    }

    /**
     * https://itqna.net/questions/15133/attach-txt-file-using-javamail
     *
     * @param filename
     * @param subject
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun addAttachment(filename: String?, subject: String?): Multipart {
        val _multipart: Multipart = MimeMultipart()
        val messageBodyPart: BodyPart = MimeBodyPart()
        val source: DataSource = FileDataSource(filename)
        messageBodyPart.dataHandler = DataHandler(source)
        messageBodyPart.fileName = filename
        _multipart.addBodyPart(messageBodyPart)
        val messageBodyPart2: BodyPart = MimeBodyPart()
        messageBodyPart2.setText(subject)
        _multipart.addBodyPart(messageBodyPart2)
        return _multipart
    }

    init {
        //if (mailHost != null) { }
        //_multipart = new MimeMultipart();
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", mailHost)
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.socketFactory.fallback"] = "false"
        props.setProperty("mail.smtp.quitwait", "false")
        gmailSession = Session.getDefaultInstance(props, this)
    }
}