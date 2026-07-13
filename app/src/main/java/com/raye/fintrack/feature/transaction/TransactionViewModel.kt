package com.raye.fintrack.feature.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.repository.AccountRepository
import com.raye.fintrack.core.repository.CategoryRepository
import com.raye.fintrack.core.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val transactions = transactionRepository.transactionsWithCategory

    val accounts = accountRepository.accounts

    val allCategories = categoryRepository.categories

    fun categories(type: TransactionType) =
        categoryRepository.getCategoriesByType(type)

    fun getTransactionDetail(id: Int) =
        transactionRepository.getTransactionDetail(id)


    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.add(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.update(transaction)
        }
    }


    fun deleteTransaction(transaction: Transaction) {

        viewModelScope.launch {
            transactionRepository.delete(transaction)
        }

    }
}