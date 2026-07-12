package com.raye.fintrack.feature.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.raye.fintrack.core.model.TransactionType

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, TransactionType) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var type by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }


    AlertDialog(

        onDismissRequest = {
            onDismiss()
        },

        title = {
            Text("Add Category")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Category name")
                    }
                )


                Row {

                    FilterChip(
                        selected = type == TransactionType.EXPENSE,
                        onClick = {
                            type = TransactionType.EXPENSE
                        },
                        label = {
                            Text("Expense")
                        }
                    )


                    FilterChip(
                        selected = type == TransactionType.INCOME,
                        onClick = {
                            type = TransactionType.INCOME
                        },
                        label = {
                            Text("Income")
                        }
                    )
                }
            }
        },


        confirmButton = {

            Button(
                onClick = {

                    if (name.isNotBlank()) {

                        onConfirm(
                            name.trim(),
                            type
                        )
                    }
                }
            ) {

                Text("Save")
            }
        },


        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        }
    )
}