package com.raye.fintrack.feature.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {

    val transactions by viewModel.transactions
        .collectAsState(initial = emptyList())


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transactions")
                }
            )
        }
    ) { padding ->


        LazyColumn(
            modifier = Modifier
                .padding(padding)
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "₹${transaction.amount / 100}"
                            )

                            Text(
                                text = formatDate(transaction.transactionDate)
                            )
                        }


                        IconButton(
                            onClick = {
                                viewModel.deleteTransaction(transaction)
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


private fun formatDate(timestamp: Long): String {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}