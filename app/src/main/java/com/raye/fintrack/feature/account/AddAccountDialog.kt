package com.raye.fintrack.feature.account

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }


    AlertDialog(

        onDismissRequest = {
            onDismiss()
        },

        title = {
            Text("Add Account")
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