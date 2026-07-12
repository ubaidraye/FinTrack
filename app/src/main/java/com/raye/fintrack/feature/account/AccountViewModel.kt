package com.raye.fintrack.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository
) : ViewModel() {

    val accounts = repository.accounts

    fun add(account: Account) {
        viewModelScope.launch {
            repository.add(account)
        }
    }

    fun update(account: Account) {
        viewModelScope.launch {
            repository.update(account)
        }
    }

    fun delete(account: Account) {
        viewModelScope.launch {
            repository.delete(account)
        }
    }
}