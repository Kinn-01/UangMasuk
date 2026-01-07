package com.example.uangmasuk.data.repository

import com.example.uangmasuk.data.local.dao.CustomerDao
import com.example.uangmasuk.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerRepository(
    private val dao: CustomerDao
) {

    fun getCustomers(): Flow<List<CustomerEntity>> =
        dao.getAllCustomers()

    suspend fun addCustomer(customer: CustomerEntity) =
        dao.insertCustomer(customer)

    suspend fun getCustomerById(id: Int): CustomerEntity? {
        return dao.getCustomerById(id)
    }

}
