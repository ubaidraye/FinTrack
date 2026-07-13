package com.raye.fintrack.feature.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.feature.transaction.TransactionViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionsWithCategory by viewModel.transactions.collectAsState(initial = emptyList())

    val expenseTransactions = transactionsWithCategory.filter { it.category?.type == TransactionType.EXPENSE }
    val totalExpense = expenseTransactions.sumOf { it.transaction.amount }

    val categoryData = expenseTransactions
        .groupBy { it.category?.name ?: "Other" }
        .mapValues { it.value.sumOf { t -> t.transaction.amount } }
        .toList()
        .sortedByDescending { it.second }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Financial Analysis") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Spending by Category", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                if (totalExpense > 0) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            PieChart(data = categoryData, total = totalExpense)
                            Spacer(modifier = Modifier.height(24.dp))
                            CategoryLegend(data = categoryData, total = totalExpense)
                        }
                    }
                } else {
                    Text("No expense data to analyze.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item {
                val income = transactionsWithCategory.filter { it.category?.type == TransactionType.INCOME }.sumOf { it.transaction.amount }
                Text("Cash Flow", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CashFlowComparison(income = income, expense = totalExpense)
                    }
                }
            }
        }
    }
}

@Composable
fun PieChart(data: List<Pair<String, Long>>, total: Long) {
    val colors = listOf(
        Color(0xFF006A6A), Color(0xFF00A3A3), Color(0xFF4EDADA), 
        Color(0xFFB1CCCC), Color(0xFF4A6363), Color(0xFFB3C8E8)
    )

    Canvas(modifier = Modifier.size(200.dp)) {
        val strokeWidth = 40.dp.toPx()
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(
            (size.width - diameter) / 2,
            (size.height - diameter) / 2
        )
        val arcSize = Size(diameter, diameter)

        var startAngle = -90f
        data.forEachIndexed { index, pair ->
            val sweepAngle = (pair.second.toFloat() / total) * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun CategoryLegend(data: List<Pair<String, Long>>, total: Long) {
    val colors = listOf(
        Color(0xFF006A6A), Color(0xFF00A3A3), Color(0xFF4EDADA), 
        Color(0xFFB1CCCC), Color(0xFF4A6363), Color(0xFFB3C8E8)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.forEachIndexed { index, pair ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(12.dp).background(colors[index % colors.size], CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(pair.first, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                Text(
                    text = "${(pair.second * 100 / total)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(formatCurrency(pair.second), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun CashFlowComparison(income: Long, expense: Long) {
    val maxVal = maxOf(income, expense, 1L)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CashFlowBar("Income", income, maxVal, Color(0xFF4CAF50))
        CashFlowBar("Expense", expense, maxVal, Color(0xFFF44336))
    }
}

@Composable
fun CashFlowBar(label: String, amount: Long, maxVal: Long, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(formatCurrency(amount), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        val fraction = amount.toFloat() / maxVal
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceAtLeast(0.01f))
                .height(12.dp)
                .background(color, MaterialTheme.shapes.small)
        )
    }
}

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"))
    return format.format(amount / 100.0)
}
