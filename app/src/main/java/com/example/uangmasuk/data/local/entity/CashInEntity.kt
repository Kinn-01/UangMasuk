package com.example.uangmasuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_in")
data class CashInEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Tanggal transaksi (dipakai untuk filter periode)
    val transactionDate: Long,

    // Nama pelanggan
    val customerName: String,

    // Keterangan transaksi
    val description: String,

    // Jumlah uang masuk (Rp)
    val amount: Long,

    // Jenis pendapatan (contoh: Pendapatan Lain, Non Pendapatan)
    val incomeType: String,

    // Path / Uri gambar bukti transfer
    val imagePath: String? = null,

    // Waktu data dibuat
    val createdAt: Long = System.currentTimeMillis()
)