package com.raye.fintrack.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val state by viewModel.dashboardState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("FinTrack")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                BalanceCard(
                    balance = state.balance
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    SummaryCard(
                        title = "Income",
                        amount = state.income,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SummaryCard(
                        title = "Expense",
                        amount = state.expense,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            item {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium
                )
            }


            items(
                state.transactions.take(5)
            ) { transaction ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = transaction.title,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = formatCurrency(transaction.amount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Long
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = formatCurrency(balance),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
private fun SummaryCard(
    title: String,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = color
            )
            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
        }
    }
}

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(amount / 100.0)
}
