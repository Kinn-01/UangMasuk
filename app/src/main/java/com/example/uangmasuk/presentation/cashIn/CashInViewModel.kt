package com.example.uangmasuk.presentation.cashIn

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.local.entity.CashInEntity
import com.example.uangmasuk.data.repository.CashInRepository
import com.example.uangmasuk.data.repository.CustomerRepository
import com.example.uangmasuk.utils.copyImageToInternalStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CashInViewModel(
    private val repository: CashInRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CashInUiState())
    val uiState: StateFlow<CashInUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CashInEvent>()
    val event = _event.asSharedFlow()


    fun onCustomerNameChange(value: String) {
        val newState = _uiState.value.copy(customerName = value)
        _uiState.value = newState.copy(isFormValid = validateForm(newState))
    }

    fun onAmountChange(value: String) {
        val newState = _uiState.value.copy(amount = value)
        _uiState.value = newState.copy(isFormValid = validateForm(newState))
    }

    fun onIncomeTypeChange(value: String) {
        val newState = _uiState.value.copy(incomeType = value)
        _uiState.value = newState.copy(isFormValid = validateForm(newState))
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }


    fun onImageSelected(context: Context, uri: Uri) {
        val path = context.copyImageToInternalStorage(uri)
        _uiState.value = _uiState.value.copy(imagePath = path)
    }

    fun onCustomerSelected(customerId: Int) {
        viewModelScope.launch {
            val customer = customerRepository.getCustomerById(customerId)
            customer?.let {
                val newState = _uiState.value.copy(
                    customerId = it.id,
                    customerName = it.name
                )
                _uiState.value = newState.copy(
                    isFormValid = validateForm(newState)
                )
            }
        }
    }

    private fun validateForm(state: CashInUiState): Boolean {
        return state.customerName.isNotBlank() &&
                state.amount.isNotBlank() &&
                state.incomeType.isNotBlank()
    }

    fun saveCashIn() {
        val state = _uiState.value

        if (!state.isFormValid) return

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

            _uiState.value = state.copy(isLoading = false)
            _event.emit(CashInEvent.Success)
        }
    }

    sealed class CashInEvent {
        object Success : CashInEvent()
    }

}