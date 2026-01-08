package com.example.uangmasuk.presentation.home

sealed class PeriodFilter {
    object Today : PeriodFilter()
    object Yesterday : PeriodFilter()
    object Last7Days : PeriodFilter()
    data class Custom(val start: Long, val end: Long) : PeriodFilter()
}
