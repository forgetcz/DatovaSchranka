package com.jvr.datovaschranka.constatns

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        /*val dateFormat: DateFormat = SimpleDateFormat(
            "hh:mm:ss dd.MM.yyyy", Locale.getDefault()
        )*/
        private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz")
        var tz = SimpleTimeZone(0, "Out Timezone")

        fun currentDateTime(): Date {
            return Date()
        }

        @Suppress("UnnecessaryVariable")
        fun currentDateTimeString(inputDate : Date): String {
            //println(SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(Date()))

            //val today = Date()

            //val timestamp: String = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(today)
            //println("timestamp = $timestamp")
            val dateResult = isoDateFormat.format(inputDate)
            return dateResult
        }

        @Suppress("UnnecessaryVariable")
        fun getDateFromString(stringDate : String?) : Date? {
            if (stringDate != null && stringDate.isNotEmpty()) {
                try {
                    val date = isoDateFormat.parse(stringDate)
                    return date
                } catch (eex : Exception) {
                    return null
                }
            } else {
                return null
            }
        }
    }
}