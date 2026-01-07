package com.example.uangmasuk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uangmasuk.data.local.dao.CashInDao
import com.example.uangmasuk.data.local.dao.CustomerDao
import com.example.uangmasuk.data.local.entity.CashInEntity
import com.example.uangmasuk.data.local.entity.CustomerEntity

@Database(
    entities = [
        CashInEntity::class,
        CustomerEntity::class
               ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashInDao(): CashInDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cash_in_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}