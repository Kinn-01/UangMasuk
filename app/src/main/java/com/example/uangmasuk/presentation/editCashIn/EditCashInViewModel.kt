package com.example.uangmasuk.presentation.editCashIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.local.entity.CashInEntity
import com.example.uangmasuk.data.repository.CashInRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditCashInViewModel(
    private val repository: CashInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditCashInUiState())
    val uiState: StateFlow<EditCashInUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<EditCashInEvent>()
    val event = _event.asSharedFlow()

    private var cashInId: Int = -1

    fun loadCashIn(id: Int) {
        viewModelScope.launch {
            val data = repository.getCashInById(id)
            data?.let {
                cashInId = it.id
                _uiState.value = EditCashInUiState(
                    customerName = it.customerName,
                    incomeType = it.incomeType,
                    amount = it.amount.toString(),
                    date = it.transactionDate.toString(),
                    description = it.description,
                    imagePath = it.imagePath,
                    isLoading = false
                )
            }
        }
    }

    fun updateCashIn() {
        viewModelScope.launch {
            val state = _uiState.value
            val entity = CashInEntity(
                id = cashInId,
                customerName = state.customerName,
                incomeType = state.incomeType,
                amount = state.amount.toLong(),
                transactionDate = state.date.toLong(),
                description = state.description,
                imagePath = state.imagePath
            )
            repository.updateCashIn(entity)
            _event.emit(EditCashInEvent.Success)
        }
    }

    fun onCustomerChange(value: String) {
        _uiState.value = _uiState.value.copy(customerName = value)
    }

    fun onAmountChange(value: String) {
        _uiState.value = _uiState.value.copy(amount = value)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value)
    }

    fun onImageChange(path: String?) {
        _uiState.value = _uiState.value.copy(imagePath = path)
    }

    fun onDateChange(value: String) {
        _uiState.value = _uiState.value.copy(date = value)
    }

    fun onIncomeTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(incomeType = value)
    }

    sealed class EditCashInEvent {
        object Success : EditCashInEvent()
    }
}