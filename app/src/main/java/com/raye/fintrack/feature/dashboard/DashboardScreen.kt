package com.raye.fintrack.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
                        amount = state.income
                    )

                    SummaryCard(
                        title = "Expense",
                        amount = state.expense
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

                        Text(transaction.title)

                        Text(
                            "₹${transaction.amount / 100.0}"
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

            Text("Balance")

            Text(
                text = "₹${balance / 100.0}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}


@Composable
private fun SummaryCard(
    title: String,
    amount: Long
) {

    Card(
        modifier = Modifier.width(160.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(title)

            Text(
                "₹${amount / 100.0}"
            )
        }
    }
}