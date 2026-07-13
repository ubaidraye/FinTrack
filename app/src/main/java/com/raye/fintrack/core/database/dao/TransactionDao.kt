package com.raye.fintrack.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.raye.fintrack.core.database.entity.Transaction as TransactionEntity
import com.raye.fintrack.core.model.TransactionDetail
import com.raye.fintrack.core.model.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM `transaction` ORDER BY transactionDate DESC")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM `transaction` ORDER BY transactionDate DESC")
    fun getTransactionsWithCategory(): Flow<List<TransactionWithCategory>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Transaction
    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getTransactionDetail(id: Int): Flow<TransactionDetail?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM `transaction` ORDER BY transactionDate DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int = 10): Flow<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM `transaction` WHERE accountId = :accountId")
    suspend fun countByAccount(accountId: Int): Int

    @Query("SELECT COUNT(*) FROM `transaction` WHERE categoryId = :categoryId")
    suspend fun countByCategory(categoryId: Int): Int
}
