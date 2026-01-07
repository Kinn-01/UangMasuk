package com.example.uangmasuk.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    fun formatRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(amount)
    }
}