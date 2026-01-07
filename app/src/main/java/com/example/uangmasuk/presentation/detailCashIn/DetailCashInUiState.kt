package com.example.uangmasuk.presentation.detailCashIn

import com.example.uangmasuk.data.local.entity.CashInEntity

data class DetailCashInUiState(
    val isLoading: Boolean = true,
    val cashIn: CashInEntity? = null
)