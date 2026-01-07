package com.example.uangmasuk.data.local.dao

import androidx.room.*
import com.example.uangmasuk.data.local.entity.CashInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashInDao {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashIn(cashIn: CashInEntity)

    // UPDATE
    @Update
    suspend fun updateCashIn(cashIn: CashInEntity)

    // DELETE
    @Delete
    suspend fun deleteCashIn(cashIn: CashInEntity)

    // READ - List (berdasarkan periode tanggal)
    @Query("""
        SELECT * FROM cash_in
        WHERE transactionDate BETWEEN :startDate AND :endDate
        ORDER BY transactionDate DESC
    """)
    fun getCashInByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<CashInEntity>>

    // READ - Detail by ID
    @Query("SELECT * FROM cash_in WHERE id = :id")
    suspend fun getCashInById(id: Int): CashInEntity?
}