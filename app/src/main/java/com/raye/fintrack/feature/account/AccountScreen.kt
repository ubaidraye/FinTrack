package com.raye.fintrack.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.model.AccountWithBalance
import com.raye.fintrack.core.ui.component.DeleteConfirmationDialog
import com.raye.fintrack.core.ui.component.EmptyState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {
    val accountsWithBalance by viewModel.accounts.collectAsState()
    var accountToDelete by remember { mutableStateOf<Account?>(null) }
    var accountToEdit by remember { mutableStateOf<Account?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (accountToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Account",
            message = "Are you sure you want to delete '${accountToDelete?.name}'? This action cannot be undone.",
            onConfirm = {
                accountToDelete?.let { account ->
                    viewModel.deleteAccount(account)
                    scope.launch {
                        snackbarHostState.showSnackbar("Account deleted")
                    }
                }
            },
            onDismiss = { accountToDelete = null }
        )
    }

    if (showAddDialog || accountToEdit != null) {
        AddAccountDialog(
            accountToEdit = accountToEdit,
            onDismiss = {
                showAddDialog = false
                accountToEdit = null
            },
            onConfirm = { name ->
                if (accountToEdit != null) {
                    viewModel.update(accountToEdit!!.copy(name = name))
                    scope.launch {
                        snackbarHostState.showSnackbar("Account updated")
                    }
                } else {
                    viewModel.addAccount(Account(name = name, icon = ""))
                    scope.launch {
                        snackbarHostState.showSnackbar("Account added")
                    }
                }
                showAddDialog = false
                accountToEdit = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accounts") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (accountsWithBalance.isEmpty()) {
            EmptyState(
                message = "No accounts found. Add your first account to get started!",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(accountsWithBalance) { item ->
                    val account = item.account
                    val balance = item.balance
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = account.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Balance: ${formatCurrency(balance)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (balance >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        accountToEdit = account
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }

                                if (!account.isDefault) {
                                    IconButton(
                                        onClick = {
                                            accountToDelete = account
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    return format.format(amount / 100.0)
}
