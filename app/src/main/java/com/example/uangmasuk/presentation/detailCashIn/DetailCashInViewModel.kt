package com.example.uangmasuk.presentation.detailCashIn

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.repository.CashInRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DetailCashInViewModel(
    private val repository: CashInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailCashInUiState())
    val uiState: StateFlow<DetailCashInUiState> = _uiState.asStateFlow()

    fun loadCashIn(id: Int) {
        viewModelScope.launch {
            val data = repository.getCashInById(id)
            _uiState.value = DetailCashInUiState(
                isLoading = false,
                cashIn = data
            )
        }
    }

    fun deleteCashIn(context: Context) {
        viewModelScope.launch {
            _uiState.value.cashIn?.let {
                it.imagePath?.let { path ->
                    val file = File(path)
                    if (file.exists()) file.delete()
                }
                repository.deleteCashIn(it)
            }
        }
    }

}