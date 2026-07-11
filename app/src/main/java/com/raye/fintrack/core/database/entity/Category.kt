package com.raye.fintrack.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raye.fintrack.core.model.TransactionType

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: TransactionType,
    val isDefault: Boolean = false,
)