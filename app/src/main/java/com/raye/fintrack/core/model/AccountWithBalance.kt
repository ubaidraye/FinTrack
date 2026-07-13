package com.raye.fintrack.core.model

import com.raye.fintrack.core.database.entity.Account

data class AccountWithBalance(
    val account: Account,
    val balance: Long
)
