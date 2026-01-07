package com.example.uangmasuk.data.repository

import com.example.uangmasuk.data.local.dao.CashInDao
import com.example.uangmasuk.data.local.entity.CashInEntity
import kotlinx.coroutines.flow.Flow

class CashInRepository (
    private val cashInDao: CashInDao
) {
    // Fungsi untuk mendapatkan daftar transaksi uang masuk berdasarkan periode tanggal
    fun getCashInByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<CashInEntity>> {
        return cashInDao.getCashInByDateRange(startDate, endDate)
    }

    // Fungsi untuk mendapatkan detail transaksi uang masuk berdasarkan ID
    suspend fun getCashInById(id: Int): CashInEntity? {
        return cashInDao.getCashInById(id)
    }

    // Fungsi untuk menambahkan transaksi uang masuk baru ke dalam database
    suspend fun insertCashIn(cashIn: CashInEntity) {
        cashInDao.insertCashIn(cashIn)
    }

    // Fungsi untuk memperbarui transaksi uang masuk yang sudah ada
    suspend fun updateCashIn(cashIn: CashInEntity) {
        cashInDao.updateCashIn(cashIn)
    }

    // Fungsi untuk menghapus transaksi uang masuk dari database
    suspend fun deleteCashIn(cashIn: CashInEntity) {
        cashInDao.deleteCashIn(cashIn)
    }
}