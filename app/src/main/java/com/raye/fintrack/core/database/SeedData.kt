package com.raye.fintrack.core.database

import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType

object SeedData {

    val accounts = listOf(
        Account(
            name = "Cash",
            icon = "cash"
        ),
        Account(
            name = "Card",
            icon = "card"
        ),
        Account(
            name = "Wallet",
            icon = "wallet"
        )
    )


    val categories = listOf(

        // Expense categories
        Category(
            name = "Other",
            type = TransactionType.EXPENSE,
            isDefault = true
        ),
        Category(
            name = "Food",
            type = TransactionType.EXPENSE,
            isDefault = true
        ),
        Category(
            name = "Transport",
            type = TransactionType.EXPENSE,
            isDefault = true
        ),
        Category(
            name = "Shopping",
            type = TransactionType.EXPENSE,
            isDefault = true
        ),
        Category(
            name = "Bills",
            type = TransactionType.EXPENSE,
            isDefault = true
        ),


        // Income categories
        Category(
            name = "Other",
            type = TransactionType.INCOME,
            isDefault = true
        ),
        Category(
            name = "Salary",
            type = TransactionType.INCOME,
            isDefault = true
        ),
        Category(
            name = "Gift",
            type = TransactionType.INCOME,
            isDefault = true
        ),
        Category(
            name = "Award",
            type = TransactionType.INCOME,
            isDefault = true
        )
    )
}