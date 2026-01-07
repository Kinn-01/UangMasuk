package com.example.uangmasuk.presentation.editCashIn

data class EditCashInUiState(
    val customerName: String = "",
    val incomeType: String = "",
    val amount: String = "",
    val date: String = "",
    val description: String = "",
    val imagePath: String? = null,
    val isLoading: Boolean = true
)