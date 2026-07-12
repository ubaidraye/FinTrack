package com.raye.fintrack.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.repository.AccountRepository
import com.raye.fintrack.core.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val accounts = repository.accounts

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

            val count = transactionRepository
                .countByAccount(account.id)

            if (count == 0) {
                repository.delete(account)
            }

        }
    }
}