package com.example.noteappwithkmm.common.utils

import com.example.noteappwithkmm.common.utils.Utils.formatDateTime
import kotlinx.datetime.*


class DataTimeUtil {
    fun now(): LocalDateTime {
        val currentMoment: Instant = Clock.System.now()
        return currentMoment.toLocalDateTime(TimeZone.UTC)
    }
     fun currentTime(): String {
        // 1
        val currentMoment: Instant = Clock.System.now()
// 2
        val dateTime: LocalDateTime =
            currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
// 3
        return formatDateTime(dateTime)
    }

    fun currentTimeZone(): String {
        val currentTimeZone = TimeZone.currentSystemDefault()
        return currentTimeZone.toString()
    }
    fun getTime(timezoneId: String): String {
        // 1
        val timezone = TimeZone.of(timezoneId)
// 2
        val currentMoment: Instant = Clock.System.now()
        // 3
        val dateTime: LocalDateTime =
            currentMoment.toLocalDateTime(timezone)
// 4
        return formatDateTime(dateTime)
    }
    fun getDate(timezoneId: String): String {
        val timezone = TimeZone.of(timezoneId)
        val currentMoment: Instant = Clock.System.now()
        val dateTime: LocalDateTime =
            currentMoment.toLocalDateTime(timezone)
// 1
        return "${
            dateTime.dayOfWeek.name.lowercase().replaceFirstChar() {
                it.uppercase()
            }
        }, " +
                "${
                    dateTime.month.name.lowercase().replaceFirstChar {
                        it.uppercase()
                    }
                }${dateTime.date.dayOfMonth}"
    }

    fun toLocalDate(date: Double): LocalDateTime {
        return Instant.fromEpochMilliseconds(date.toLong()).toLocalDateTime(TimeZone.UTC)
    }

    fun toEpochMilliseconds(date: LocalDateTime): Double {
        return date.toInstant(TimeZone.UTC).toEpochMilliseconds().toDouble()
    }

    // States: yesterday, today, tomorrow and everything else
    fun humanizeDatetime(date: LocalDateTime?): String {
        val sb = StringBuilder()


        date?.run {
            try {
                val hour = if (this.hour > 12) {
                    (this.hour - 12).toString() + "pm"
                } else {
                    if (this.hour != 0) this.hour.toString() + "am" else "midnight"
                }
                val today = now()
                val currentMoment: Instant = Clock.System.now()

                val tomorrow = currentMoment.plus(1, DateTimeUnit.DAY, TimeZone.UTC)
                    .toLocalDateTime(TimeZone.UTC)
                if (this.date == today.date) {
                    sb.append("Today at $hour")
                } else if (this.date == tomorrow.date) {
                    sb.append("Tomorrow at $hour")
                } else {
                    sb.append(this.date.month.name.lowercase() + " ${this.date.dayOfMonth}")
                }
            } catch (e: Exception) {
                println("ERROR WITH ${e.message.toString()}")
            }
        } ?: sb.append("Unknown")
        return sb.toString()
    }
}