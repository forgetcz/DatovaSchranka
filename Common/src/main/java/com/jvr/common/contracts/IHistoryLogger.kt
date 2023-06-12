package com.jvr.common.contracts

interface IHistoryLogger {
    fun getMessages(): List<ILogMessageItem?>?
}