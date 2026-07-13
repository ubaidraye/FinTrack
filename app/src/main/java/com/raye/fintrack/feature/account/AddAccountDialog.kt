package com.raye.fintrack.feature.account

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.raye.fintrack.core.database.entity.Account

@Composable
fun AddAccountDialog(
    accountToEdit: Account? = null,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    var name by remember {
        mutableStateOf(accountToEdit?.name ?: "")
    }


    AlertDialog(

        onDismissRequest = {
            onDismiss()
        },

        title = {
            Text(if (accountToEdit == null) "Add Account" else "Edit Account")
        },

        text = {

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Account name")
                },
                singleLine = true
            )
        },


        confirmButton = {

            Button(
                onClick = {

                    if (name.isNotBlank()) {
                        onConfirm(name.trim())
                    }

                }
            ) {
                Text("Save")
            }
        },


        dismissButton = {

            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}