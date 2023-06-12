package com.jvr.common.lib.logger

import com.jvr.common.contracts.ILogMessageItem
import java.text.DateFormat
import java.util.*

class LogMessageItem(stack:StackTraceElement, private var tag: String,
                     private val message: String, msgType:MessageType)
    : ILogMessageItem {

    enum class MessageType { Info, Debug, Warning, Error }

    private var dateInsert: Date = Date()
    private val messageType: MessageType = msgType
    private val methodName: String = stack.methodName
    private val className: String = stack.className
    private val fileName: String = stack.fileName

    override fun getDateInsert(): String {
        return dateInsert.toString()
    }
    override fun getTag(): String { return tag }
    override fun getMessage(): String { return message }
    override fun getMethodName(): String { return methodName }
    override fun getClassName(): String { return className }
    override fun getFileName(): String { return fileName }

    override fun toString(): String {
        //String pattern = "yyyy-MM-dd HH:mm:ss";
        val df = DateFormat.getTimeInstance() // new SimpleDateFormat(pattern);
        val todayAsString = df.format(dateInsert)
        return """
             LogMessageItem{dateInsert=$todayAsString, Tag='${getTag()}
             , MethodName='${getMethodName()}
             , Class='${getMessage()}
             , FileName='${getMessage()}
             , msgType='$messageType
             }
             """.trimIndent()
    }

}