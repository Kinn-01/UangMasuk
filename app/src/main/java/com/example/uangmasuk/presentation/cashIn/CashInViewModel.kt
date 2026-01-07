package com.example.uangmasuk.presentation.cashIn

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.local.entity.CashInEntity
import com.example.uangmasuk.data.local.entity.CustomerEntity
import com.example.uangmasuk.data.repository.CashInRepository
import com.example.uangmasuk.data.repository.CustomerRepository
import com.example.uangmasuk.utils.copyImageToInternalStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashInViewModel(
    private val repository: CashInRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CashInUiState())
    val uiState: StateFlow<CashInUiState> = _uiState.asStateFlow()

    fun onCustomerNameChange(value: String) {
        _uiState.value = _uiState.value.copy(customerName = value)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }

    fun onAmountChange(value: String) {
        _uiState.value = _uiState.value.copy(amount = value)
    }

    fun onIncomeTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(incomeType = value)
    }

    fun onImageSelected(context: Context, uri: Uri) {
        val path = context.copyImageToInternalStorage(uri)
        _uiState.value = _uiState.value.copy(imagePath = path)
    }

    fun onCustomerSelected(customerId: Int) {
        viewModelScope.launch {
            val customer = customerRepository.getCustomerById(customerId)
            customer?.let {
                _uiState.update { state ->
                    state.copy(
                        customerId = it.id,
                        customerName = it.name
                    )
                }
            }
        }
    }


    fun saveCashIn() {
        val state = _uiState.value

        if (
            state.customerName.isBlank() ||
            state.amount.isBlank() ||
            state.incomeType.isBlank()
        ) return

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)

            repository.insertCashIn(
                CashInEntity(
                    transactionDate = state.transactionDate,
                    customerName = state.customerName,
                    description = state.description,
                    amount = state.amount.toLong(),
                    incomeType = state.incomeType,
                    imagePath = state.imagePath
                )
            )

            _uiState.value = state.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }
}