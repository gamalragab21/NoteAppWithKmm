package com.example.noteappwithkmm.common.utils

import kotlinx.datetime.LocalDateTime

object Utils {
    fun formatDateTime(dateTime: LocalDateTime): String {
// 1
        val stringBuilder = StringBuilder()
// 2
        var hour = dateTime.hour
        val minute = dateTime.minute
        var amPm = " am"
// 3
// For 12
        if (hour > 12) {
            amPm = " pm"
            hour -= 12
        }
// 4
        stringBuilder.append(hour.toString())
        stringBuilder.append(":")
// 5
        if (minute < 10) {
            stringBuilder.append('0')
        }
        stringBuilder.append(minute.toString())
        stringBuilder.append(amPm)
// 6
        return stringBuilder.toString()
    }

}