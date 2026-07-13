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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.ui.component.TransactionTypeChip
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
        mutableLongStateOf(transactionToEdit?.transactionDate ?: System.currentTimeMillis())
    }

    var type by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = transactionDate
    )

    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().apply { timeInMillis = transactionDate }.get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().apply { timeInMillis = transactionDate }.get(Calendar.MINUTE)
    )

    val accounts by viewModel.accounts.collectAsState(
        initial = emptyList()
    )

    var selectedAccountId by remember {
        mutableStateOf(transactionToEdit?.accountId)
    }

    var selectedCategoryId by remember {
        mutableStateOf(transactionToEdit?.categoryId)
    }

    val categories by viewModel.categories(type)
        .collectAsState(initial = emptyList())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = datePickerState.selectedDateMillis ?: transactionDate
                        val currentTime = Calendar.getInstance().apply { timeInMillis = transactionDate }
                        set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
                        set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE))
                    }
                    transactionDate = calendar.timeInMillis
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

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = transactionDate
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    transactionDate = calendar.timeInMillis
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    val allCategories by viewModel.allCategories.collectAsState(initial = emptyList())

    LaunchedEffect(transactionToEdit, allCategories) {
        if (transactionToEdit != null && allCategories.isNotEmpty()) {
            val category = allCategories.find { it.id == transactionToEdit.categoryId }
            category?.type?.let { 
                type = it
            }
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
                TransactionTypeChip(
                    type = TransactionType.EXPENSE,
                    selected = type == TransactionType.EXPENSE,
                    onClick = {
                        type = TransactionType.EXPENSE
                        selectedCategoryId = null
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )

                TransactionTypeChip(
                    type = TransactionType.INCOME,
                    selected = type == TransactionType.INCOME,
                    onClick = {
                        type = TransactionType.INCOME
                        selectedCategoryId = null
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(transactionDate)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Time") },
                    trailingIcon = {
                        IconButton(onClick = { showTimePicker = true }) {
                            Icon(Icons.Default.Schedule, contentDescription = "Select Time")
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }


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