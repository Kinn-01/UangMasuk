package com.example.uangmasuk.di

import android.content.Context
import com.example.uangmasuk.data.local.database.AppDatabase
import com.example.uangmasuk.data.repository.CashInRepository
import com.example.uangmasuk.data.repository.CustomerRepository

object Injection {

    fun provideCashInRepository(context: Context): CashInRepository {
        val database = AppDatabase.getInstance(context)
        return CashInRepository(database.cashInDao())
    }

    fun provideCustomerRepository(context: Context): CustomerRepository {
        val database = AppDatabase.getInstance(context)
        return CustomerRepository(database.customerDao())
    }

}