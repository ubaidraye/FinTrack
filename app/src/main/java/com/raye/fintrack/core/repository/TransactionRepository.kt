package com.raye.fintrack.core.repository

import com.raye.fintrack.core.database.dao.TransactionDao
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {

    val transactions: Flow<List<Transaction>> =
        dao.getTransactions()

    val transactionsWithCategory: Flow<List<TransactionWithCategory>> =
        dao.getTransactionsWithCategory()

    fun recentTransactions(limit: Int = 10): Flow<List<Transaction>> {
        return dao.getRecentTransactions(limit)
    }

    suspend fun getTransaction(id: Int) =
        dao.getTransactionById(id)

    suspend fun add(transaction: Transaction) =
        dao.insert(transaction)

    suspend fun update(transaction: Transaction) =
        dao.update(transaction)

    suspend fun delete(transaction: Transaction) =
        dao.delete(transaction)

    suspend fun countByAccount(accountId: Int) =
        dao.countByAccount(accountId)

    suspend fun countByCategory(categoryId: Int) =
        dao.countByCategory(categoryId)
}