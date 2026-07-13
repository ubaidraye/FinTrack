package com.raye.fintrack.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.raye.fintrack.core.model.TransactionType

@Composable
fun TransactionTypeChip(
    type: TransactionType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (type == TransactionType.INCOME) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFF44336)
    }

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = if (type == TransactionType.INCOME) "Income" else "Expense",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = Color.White,
            containerColor = color.copy(alpha = 0.1f),
            labelColor = color
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            selectedBorderColor = color,
            borderColor = color.copy(alpha = 0.5f)
        ),
        modifier = modifier
    )
}
