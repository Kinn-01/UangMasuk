package com.example.uangmasuk.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.repository.CashInRepository
import com.example.uangmasuk.utils.DateUtils
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val repository: CashInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentStartDate: Long = 0L
    private var currentEndDate: Long = Long.MAX_VALUE

    init {
        applyPeriod(PeriodFilter.Today)
    }

    fun applyPeriod(period: PeriodFilter) {
        val (start, end) = when (period) {
            PeriodFilter.Today -> DateUtils.todayRange()
            PeriodFilter.Yesterday -> DateUtils.yesterdayRange()
            PeriodFilter.Last7Days -> DateUtils.last7DaysRange()
            is PeriodFilter.Custom ->
                DateUtils.startOfDay(period.start) to
                        DateUtils.endOfDay(period.end)

        }

        _uiState.update { it.copy(isLoading = true, period = period) }

        repository.getCashInByDateRange(start, end)
            .onEach { list ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        cashInList = list
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}