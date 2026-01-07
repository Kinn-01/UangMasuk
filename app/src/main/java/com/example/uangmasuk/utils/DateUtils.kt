package com.example.uangmasuk.utils

import com.example.uangmasuk.data.local.entity.CashInEntity
import java.text.SimpleDateFormat
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

    fun List<CashInEntity>.groupByDate(): Map<String, List<CashInEntity>> {
        return this.groupBy {
            DateUtils.formatDate(it.transactionDate)
        }
    }

}