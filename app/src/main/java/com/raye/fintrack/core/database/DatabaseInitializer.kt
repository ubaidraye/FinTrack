package com.raye.fintrack.core.database

import com.raye.fintrack.core.database.dao.AccountDao
import com.raye.fintrack.core.database.dao.CategoryDao
import com.raye.fintrack.core.database.dao.TransactionDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) {
    suspend fun initialize() {
        if (accountDao.count() == 0) {
            accountDao.insertAll(SeedData.accounts)
        }

        if (categoryDao.count() == 0) {
            categoryDao.insertAll(SeedData.categories)
        }

        if (transactionDao.count() == 0) {
            SeedData.sampleTransactions().forEach { transaction ->
                transactionDao.insert(transaction)
            }
        }
    }
}
