package com.raye.fintrack.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val amount: Long,
    val accountId: Int,
    val categoryId: Int,
    val transactionDate: Long
)