package com.ferelin.novaposhtanews.utils.locale

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val NEWS_DATE_FORMAT = "d MMM yyyy"
const val NULL_TIMESTAMP = 0L

class AppDateUtils(
    private val newsDateFormat: SimpleDateFormat,
    private val newsMdDateFormat: SimpleDateFormat,
    private val newsUaDateFormat: SimpleDateFormat,
) {

    fun timestampToDateStr(timestamp: Long): String {
        val date = Date(timestamp)
        return newsDateFormat.format(date)
    }

    fun newsMdDateToTimestamp(dateStr: String): Long {
        return try {
            newsMdDateFormat.parse(dateStr)?.time ?: NULL_TIMESTAMP
        } catch (e: ParseException) {
            NULL_TIMESTAMP
        }
    }

    fun newsUaDateToTimestamp(dateStr: String): Long {
        return try {
            newsUaDateFormat.parse(dateStr)?.time ?: NULL_TIMESTAMP
        } catch (e: ParseException) {
            NULL_TIMESTAMP
        }
    }
}
