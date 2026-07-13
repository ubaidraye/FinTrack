package com.raye.fintrack.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raye.fintrack.core.database.converter.Converters
import com.raye.fintrack.core.database.dao.AccountDao
import com.raye.fintrack.core.database.dao.CategoryDao
import com.raye.fintrack.core.database.dao.TransactionDao
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.database.entity.Transaction

@Database(
    entities = [
        Account::class,
        Category::class,
        Transaction::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinTrackDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    fun clearAllData() {
        clearAllTables()
    }
}
