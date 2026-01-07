package com.example.uangmasuk.presentation.custList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uangmasuk.data.local.entity.CustomerEntity

import com.example.uangmasuk.data.repository.CustomerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val repository: CustomerRepository
) : ViewModel() {

    val customers = repository.getCustomers()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun addCustomers(
        name: String,
        phone: String,
        email: String,
        isMember: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.addCustomer(
                CustomerEntity(
                    name = name,
                    phone = phone.ifBlank { null },
                    email = email.ifBlank { null },
                    isMember = isMember
                )
            )
            onSuccess()
        }
    }


}

