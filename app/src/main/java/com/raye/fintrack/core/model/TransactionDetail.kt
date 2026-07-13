package com.raye.fintrack.core.model

import androidx.room.Embedded
import androidx.room.Relation
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.database.entity.Transaction

data class TransactionDetail(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category?,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: Account?
)
