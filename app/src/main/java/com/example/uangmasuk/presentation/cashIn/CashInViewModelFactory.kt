package com.example.uangmasuk.presentation.cashIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uangmasuk.data.repository.CashInRepository
import com.example.uangmasuk.data.repository.CustomerRepository

class CashInViewModelFactory(
    private val cashInRepository: CashInRepository,
    private val customerRepository: CustomerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CashInViewModel::class.java)) {
            return CashInViewModel(
                cashInRepository,
                customerRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
