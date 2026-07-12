package com.raye.fintrack.feature.transaction

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    var title by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var type by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }

    val accounts by viewModel.accounts.collectAsState(
        initial = emptyList()
    )

    val categories by viewModel.categories(type)
        .collectAsState(initial = emptyList())

    var selectedAccountId by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedCategoryId by remember {
        mutableStateOf<Int?>(null)
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
                    Text("Add Transaction")
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
                }
            )


            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                },
                label = {
                    Text("Amount")
                }
            )


            Button(
                onClick = {
                    val amountValue = amount.toLongOrNull()

                    if (amountValue == null || amountValue <= 0) {
                        return@Button
                    }


                    if (
                        selectedAccountId == null ||
                        selectedCategoryId == null
                    ) {
                        return@Button
                    }
                    val transaction = Transaction(
                        title = title,
                        amount = amountValue * 100,
                        accountId = selectedAccountId!!,
                        categoryId = selectedCategoryId!!,
                        transactionDate = System.currentTimeMillis()
                    )
                    viewModel.addTransaction(transaction)

                    onBack()
                }
            ) {

                Text("Save")

            }
        }
    }
}