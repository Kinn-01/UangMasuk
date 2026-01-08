package com.example.uangmasuk.utils

import com.example.uangmasuk.data.local.entity.CashInEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            Locale("id", "ID")
        )
        return sdf.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    fun formatDateRange(start: Long, end: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        return "${sdf.format(Date(start))} - ${sdf.format(Date(end))}"
    }

    fun List<CashInEntity>.groupByDate(): Map<String, List<CashInEntity>> {
        return this.groupBy {
            formatDate(it.transactionDate)
        }
    }

    fun todayRange(): Pair<Long, Long> {
        val now = System.currentTimeMillis()
        return startOfDay(now) to endOfDay(now)
    }

    fun yesterdayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val time = cal.timeInMillis
        return startOfDay(time) to endOfDay(time)
    }

    fun last7DaysRange(): Pair<Long, Long> {
        val end = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -6)
        val start = cal.timeInMillis
        return startOfDay(start) to endOfDay(end)
    }

    fun startOfDay(time: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun endOfDay(time: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

}