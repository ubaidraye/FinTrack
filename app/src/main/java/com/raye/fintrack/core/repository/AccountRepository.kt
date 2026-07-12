package com.raye.fintrack.core.repository

import com.raye.fintrack.core.database.dao.AccountDao
import com.raye.fintrack.core.database.entity.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val dao: AccountDao
) {

    val accounts: Flow<List<Account>> = dao.getAccounts()

    suspend fun getAccount(id: Int) = dao.getAccountById(id)

    suspend fun add(account: Account) = dao.insert(account)

    suspend fun update(account: Account) = dao.update(account)

    suspend fun delete(account: Account) = dao.delete(account)

    suspend fun exists(name: String) = dao.exists(name)
}