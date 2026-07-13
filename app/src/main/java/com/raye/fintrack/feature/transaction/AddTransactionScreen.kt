package com.raye.fintrack.feature.transaction

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTransactionScreen(
    transactionToEdit: Transaction? = null,
    onBack: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    var title by remember {
        mutableStateOf(transactionToEdit?.title ?: "")
    }

    var description by remember {
        mutableStateOf(transactionToEdit?.description ?: "")
    }

    var amount by remember {
        mutableStateOf(transactionToEdit?.amount?.let { (it / 100.0).toString() } ?: "")
    }

    var transactionDate by remember {
        mutableStateOf(transactionToEdit?.transactionDate ?: System.currentTimeMillis())
    }

    var type by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = transactionDate
    )

    val accounts by viewModel.accounts.collectAsState(
        initial = emptyList()
    )

    var selectedAccountId by remember {
        mutableStateOf<Int?>(transactionToEdit?.accountId)
    }

    var selectedCategoryId by remember {
        mutableStateOf<Int?>(transactionToEdit?.categoryId)
    }

    val categories by viewModel.categories(type)
        .collectAsState(initial = emptyList())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    transactionDate = datePickerState.selectedDateMillis ?: transactionDate
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // If editing, we need to know the type of the transaction's category to set the initial 'type' filter
    LaunchedEffect(transactionToEdit) {
        if (transactionToEdit != null) {
            // This is a bit tricky because categories are filtered by type.
            // In a real app, we might need a getCategoryById(id) call.
        }
    }

    var accountExpanded by remember {
        mutableStateOf(false)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (transactionToEdit == null) "Add Transaction" else "Edit Transaction")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                FilterChip(
                    selected = type == TransactionType.EXPENSE,
                    onClick = {
                        type = TransactionType.EXPENSE
                        selectedCategoryId = null
                    },
                    label = {
                        Text("Expense")
                    }
                )

                FilterChip(
                    selected = type == TransactionType.INCOME,
                    onClick = {
                        type = TransactionType.INCOME
                        selectedCategoryId = null
                    },
                    label = {
                        Text("Income")
                    }
                )
            }

            ExposedDropdownMenuBox(
                expanded = accountExpanded,
                onExpandedChange = {
                    accountExpanded = !accountExpanded
                }
            ) {

                val selectedAccount =
                    accounts.find { it.id == selectedAccountId }

                OutlinedTextField(
                    value = selectedAccount?.name ?: "Select Account",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Account")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = accountExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = accountExpanded,
                    onDismissRequest = {
                        accountExpanded = false
                    }
                ) {

                    accounts.forEach { account ->

                        DropdownMenuItem(
                            text = {
                                Text(account.name)
                            },
                            onClick = {

                                selectedAccountId = account.id
                                accountExpanded = false

                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = {
                    categoryExpanded = !categoryExpanded
                }
            ) {

                val selectedCategory =
                    categories.find { it.id == selectedCategoryId }


                OutlinedTextField(
                    value = selectedCategory?.name ?: "Select Category",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Category")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = categoryExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )


                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    }
                ) {

                    categories.forEach { category ->

                        DropdownMenuItem(
                            text = {
                                Text(category.name)
                            },
                            onClick = {

                                selectedCategoryId = category.id
                                categoryExpanded = false

                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = {
                    Text("Title")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                label = {
                    Text("Description (Optional)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.isEmpty() || it.toDoubleOrNull() != null || it.toLongOrNull() != null) {
                        amount = it
                    }
                },
                label = {
                    Text("Amount")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(transactionDate)),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    val amountValue = amount.replace(",", ".").toDoubleOrNull()

                    if (amountValue == null || amountValue <= 0) {
                        return@Button
                    }

                    if (selectedAccountId == null || selectedCategoryId == null) {
                        return@Button
                    }

                    val transaction = Transaction(
                        id = transactionToEdit?.id ?: 0,
                        title = title,
                        description = description.takeIf { it.isNotBlank() },
                        amount = (amountValue * 100).toLong(),
                        accountId = selectedAccountId!!,
                        categoryId = selectedCategoryId!!,
                        transactionDate = transactionDate
                    )
                    if (transactionToEdit == null) {
                        viewModel.addTransaction(transaction)
                    } else {
                        viewModel.updateTransaction(transaction)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && amount.isNotBlank() && selectedAccountId != null && selectedCategoryId != null
            ) {
                Text("Save Transaction")
            }
        }
    }
}