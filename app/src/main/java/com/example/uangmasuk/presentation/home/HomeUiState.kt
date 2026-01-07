package com.example.uangmasuk.presentation.home

import com.example.uangmasuk.data.local.entity.CashInEntity

data class HomeUiState(
    val isLoading: Boolean = false,
    val cashInList: List<CashInEntity> = emptyList()
)