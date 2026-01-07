package com.example.uangmasuk.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.repository.CashInRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val repository: CashInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentStartDate: Long = 0L
    private var currentEndDate: Long = Long.MAX_VALUE

    init {
        loadCashIn()
    }

    fun loadCashIn(
        startDate: Long = currentStartDate,
        endDate: Long = currentEndDate
    ) {
        currentStartDate = startDate
        currentEndDate = endDate

        repository.getCashInByDateRange(startDate, endDate)
            .onEach { list ->
                _uiState.value = HomeUiState(
                    isLoading = false,
                    cashInList = list
                )
            }
            .launchIn(viewModelScope)
    }
}