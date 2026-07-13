package com.raye.fintrack.core.model

import androidx.room.Embedded
import androidx.room.Relation
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.database.entity.Transaction

data class TransactionWithCategory(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category?
)
