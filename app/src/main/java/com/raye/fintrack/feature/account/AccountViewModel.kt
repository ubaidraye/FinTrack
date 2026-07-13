package com.raye.fintrack.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.model.AccountWithBalance
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.repository.AccountRepository
import com.raye.fintrack.core.repository.CategoryRepository
import com.raye.fintrack.core.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val accounts: StateFlow<List<AccountWithBalance>> =
        combine(
            repository.accounts,
            transactionRepository.transactions,
            categoryRepository.categories
        ) { accounts, transactions, categories ->
            accounts.map { account ->
                val accountTransactions = transactions.filter { it.accountId == account.id }
                var balance = 0L
                accountTransactions.forEach { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    when (category?.type) {
                        TransactionType.INCOME -> balance += transaction.amount
                        TransactionType.EXPENSE -> balance -= transaction.amount
                        null -> {}
                    }
                }
                AccountWithBalance(account, balance)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addAccount(account: Account) {
        viewModelScope.launch {
            repository.add(account)
        }
    }

    fun update(account: Account) {
        viewModelScope.launch {
            repository.update(account)
        }
    }

    fun deleteAccount(account: Account) {

        viewModelScope.launch {

            if (account.isDefault) {
                return@launch
            }

            val count = transactionRepository
                .countByAccount(account.id)

            if (count == 0) {
                repository.delete(account)
            }

        }
    }
}