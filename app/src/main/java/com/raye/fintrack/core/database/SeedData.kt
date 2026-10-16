package com.raye.fintrack.core.database

import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType

object SeedData {

    val accounts = listOf(
        Account(
            name = "Cash",
            icon = "cash",
            isDefault = true
        ),
        Account(
            name = "Card",
            icon = "card",
            isDefault = true
        ),
        Account(
            name = "Wallet",
            icon = "wallet",
            isDefault = true
        )
    )


    val categories = listOf(
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

    fun sampleTransactions(): List<Transaction> {
        val now = System.currentTimeMillis()
        val day = 86400000L
        return listOf(
            Transaction(
                title = "Monthly Salary",
                description = "Tech Corp Payroll",
                amount = 7500000L,
                accountId = 2,
                categoryId = 7,
                transactionDate = now - (1 * day)
            ),
            Transaction(
                title = "Grocery Shopping",
                description = "Weekly essentials at Supermarkt",
                amount = 345000L,
                accountId = 1,
                categoryId = 2,
                transactionDate = now - (2 * day)
            ),
            Transaction(
                title = "Electricity & Internet Bill",
                description = "Monthly utility payments",
                amount = 1800000L,
                accountId = 2,
                categoryId = 5,
                transactionDate = now - (3 * day)
            ),
            Transaction(
                title = "Freelance Design Stipend",
                description = "UI Design Consultation",
                amount = 1250000L,
                accountId = 3,
                categoryId = 8,
                transactionDate = now - (4 * day)
            ),
            Transaction(
                title = "Weekend Apparel",
                description = "New clothes and shoes",
                amount = 520000L,
                accountId = 2,
                categoryId = 4,
                transactionDate = now - (5 * day)
            ),
            Transaction(
                title = "Fuel Refill",
                description = "Petrol station",
                amount = 185000L,
                accountId = 1,
                categoryId = 3,
                transactionDate = now - (6 * day)
            ),
            Transaction(
                title = "Dinner with Friends",
                description = "Italian restaurant",
                amount = 210000L,
                accountId = 2,
                categoryId = 2,
                transactionDate = now - (7 * day)
            )
        )
    }
}