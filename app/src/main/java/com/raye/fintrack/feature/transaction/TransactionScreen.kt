package com.raye.fintrack.feature.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Transaction
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.ui.component.EmptyState
import com.raye.fintrack.core.ui.component.TransactionTypeChip
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onTransactionClick: (Transaction) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val transactionsWithCategory by viewModel.transactions
        .collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }

    val filteredTransactions = remember(transactionsWithCategory, searchQuery) {
        if (searchQuery.isBlank()) {
            transactionsWithCategory
        } else {
            transactionsWithCategory.filter {
                it.transaction.title.contains(searchQuery, ignoreCase = true) ||
                        it.category?.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transactions")
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search transactions...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            if (filteredTransactions.isEmpty()) {
                EmptyState(
                    message = if (searchQuery.isBlank()) "No transactions yet. Start tracking your expenses and income!" else "No transactions found for '$searchQuery'",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTransactions) { item ->
                        val transaction = item.transaction
                        val category = item.category
                        val isIncome = category?.type == TransactionType.INCOME
                        val amountPrefix = if (isIncome) "+" else "-"
                        val amountColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTransactionClick(transaction) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = transaction.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        category?.type?.let { type ->
                                            TransactionTypeChip(
                                                type = type,
                                                selected = true,
                                                onClick = { onTransactionClick(transaction) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "$amountPrefix ${formatCurrency(transaction.amount)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = amountColor
                                    )

                                    Text(
                                        text = formatDate(transaction.transactionDate),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    return format.format(amount / 100.0)
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}
