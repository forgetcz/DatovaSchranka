package com.jvr.common.lib.logger

import com.jvr.common.contracts.ILogger
import com.jvr.common.contracts.IHistoryLogger
import com.jvr.common.contracts.ILogMessageItem
import com.jvr.common.contracts.BaseActivityClass
import java.lang.Exception
import java.util.ArrayList

/**
 * Save all message to private List of messages
 */
class HistoryLogger : ILogger, IHistoryLogger {

    private val maxMessages = 100
    private val list: MutableList<ILogMessageItem?> = ArrayList(maxMessages)

    private fun clear() {
        list.clear()
    }
    /**
     * Add message to queue
     * @param tag Message tag
     * @param message Message
     */
    private fun addMessage(
        stack: StackTraceElement, tag: String, message: String?, msgType: LogMessageItem.MessageType
    ) {
        val item = LogMessageItem(stack, tag, message!!, msgType)
        list.add(item)
        while (list.size > maxMessages) {
            list.removeAt(0)
        }
        //return item;
    }

    override fun getMessages(): List<ILogMessageItem?> {
        return list
    }

    override fun d(context: BaseActivityClass, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, context.getTag(), message, LogMessageItem.MessageType.Debug)
    }

    override fun d(tag: String, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, tag, message, LogMessageItem.MessageType.Debug)
    }

    override fun i(context: BaseActivityClass, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, context.getTag(), message, LogMessageItem.MessageType.Info)
    }

    override fun i(tag: String, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, tag, message, LogMessageItem.MessageType.Info)
    }

    override fun w(context: BaseActivityClass, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, context.getTag(), message, LogMessageItem.MessageType.Warning)
    }

    override fun w(tag: String, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, tag, message, LogMessageItem.MessageType.Warning)
    }

    override fun e(context: BaseActivityClass, message: String) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, context.getTag(), message, LogMessageItem.MessageType.Error)
    }

    override fun e(context: BaseActivityClass, message: Exception) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, context.getTag(), message.message, LogMessageItem.MessageType.Error)
    }

    override fun e(tag: String, message: Exception) {
        val stackTrace = Exception().stackTrace[1]
        addMessage(stackTrace, tag, message.message, LogMessageItem.MessageType.Error)
    }

}