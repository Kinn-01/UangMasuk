package com.example.uangmasuk.presentation.cashIn

data class CashInUiState (
    val customerName: String = "",
    val customerId: Int? = null,
    val description: String = "",
    val amount: String = "",
    val incomeType: String = "",
    val transactionDate: Long = System.currentTimeMillis(),
    val imagePath: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)