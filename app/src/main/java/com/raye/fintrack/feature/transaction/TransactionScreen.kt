package com.raye.fintrack.feature.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.ui.component.DeleteConfirmationDialog
import com.raye.fintrack.core.ui.component.EmptyState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {

    val transactions by viewModel.transactions
        .collectAsState(initial = emptyList())

    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (transactionToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Transaction",
            message = "Are you sure you want to delete this transaction?",
            onConfirm = {
                transactionToDelete?.let { transaction ->
                    viewModel.deleteTransaction(transaction)
                    scope.launch {
                        snackbarHostState.showSnackbar("Transaction deleted")
                    }
                }
            },
            onDismiss = { transactionToDelete = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transactions")
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (transactions.isEmpty()) {
            EmptyState(
                message = "No transactions yet. Start tracking your expenses and income!",
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

                items(transactions) { transaction ->

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

                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    text = transaction.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = formatCurrency(transaction.amount),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = formatDate(transaction.transactionDate),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }


                            IconButton(
                                onClick = {
                                    transactionToDelete = transaction
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

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    return format.format(amount / 100.0)
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}
