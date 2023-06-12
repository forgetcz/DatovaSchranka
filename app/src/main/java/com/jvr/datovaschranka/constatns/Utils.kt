package com.jvr.datovaschranka.constatns

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    /*val dateFormat: DateFormat = SimpleDateFormat(
        "hh:mm:ss dd.MM.yyyy", Locale.getDefault()
    )*/
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
    fun currentDateTime(): Date {
        return Date()
    }

    fun currentDateTimeString(): String {
        return isoDateFormat.format(Date())
    }
}