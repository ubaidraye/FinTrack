package com.raye.fintrack.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raye.fintrack.core.database.entity.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT COUNT(*) FROM account")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(accounts: List<Account>)

    @Query("SELECT * FROM account ORDER BY name ASC")
    fun getAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM account WHERE id = :id")
    suspend fun getAccountById(id: Int): Account?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: Account)

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("SELECT EXISTS(SELECT 1 FROM account WHERE LOWER(name) = LOWER(:name))")
    suspend fun exists(name: String): Boolean
}