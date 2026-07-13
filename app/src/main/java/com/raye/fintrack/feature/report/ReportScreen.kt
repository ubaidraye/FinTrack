package com.raye.fintrack.feature.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.feature.transaction.TransactionViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionsWithCategory by viewModel.transactions.collectAsState(initial = emptyList())

    val income = transactionsWithCategory.filter { it.category?.type == TransactionType.INCOME }.sumOf { it.transaction.amount }
    val expense = transactionsWithCategory.filter { it.category?.type == TransactionType.EXPENSE }.sumOf { it.transaction.amount }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Financial Reports") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Overall Summary", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ReportSummaryCard("Income", income, Color(0xFF4CAF50), Modifier.weight(1f))
                    ReportSummaryCard("Expense", expense, Color(0xFFF44336), Modifier.weight(1f))
                }
            }

            item {
                Text("Spending by Category", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                val categorySpending = transactionsWithCategory
                    .filter { it.category?.type == TransactionType.EXPENSE }
                    .groupBy { it.category?.name ?: "Other" }
                    .mapValues { it.value.sumOf { t -> t.transaction.amount } }

                if (categorySpending.isEmpty()) {
                    Text("No expenses recorded yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    SimpleBarChart(categorySpending)
                }
            }
        }
    }
}

@Composable
fun ReportSummaryCard(title: String, amount: Long, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(formatCurrency(amount), style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SimpleBarChart(data: Map<String, Long>) {
    val maxVal = data.values.maxOrNull() ?: 1L
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.forEach { (category, amount) ->
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(category, style = MaterialTheme.typography.bodySmall)
                    Text(formatCurrency(amount), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                val fraction = amount.toFloat() / maxVal
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    return format.format(amount / 100.0)
}
