package com.raye.fintrack.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.model.TransactionWithCategory
import com.raye.fintrack.core.repository.CategoryRepository
import com.raye.fintrack.core.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    val recentTransactions = transactionRepository.recentTransactions()

    val dashboardState: StateFlow<DashboardState> =
        combine(
            transactionRepository.transactionsWithCategory,
            categoryRepository.categories
        ) { transactionsWithCategory, categories ->

            var income = 0L
            var expense = 0L

            transactionsWithCategory.forEach { item ->
                val transaction = item.transaction
                val category = item.category

                when (category?.type) {
                    TransactionType.INCOME -> income += transaction.amount
                    TransactionType.EXPENSE -> expense += transaction.amount
                    null -> {}
                }
            }

            DashboardState(
                income = income,
                expense = expense,
                balance = income - expense,
                transactions = transactionsWithCategory
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DashboardState()
        )
}

data class DashboardState(
    val income: Long = 0,
    val expense: Long = 0,
    val balance: Long = 0,
    val transactions: List<TransactionWithCategory> = emptyList()
)
