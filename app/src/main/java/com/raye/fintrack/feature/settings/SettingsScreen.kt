package com.raye.fintrack.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.DatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Data") },
            text = { Text("This will delete all your transactions, custom categories, and accounts. Default data will be restored. Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetDatabase()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                SettingsHeader("Data Management")
            }
            item {
                SettingsItem(
                    title = "Reset All Data",
                    subtitle = "Clear all transactions and custom data",
                    icon = Icons.Default.DeleteForever,
                    onClick = { showResetDialog = true },
                    textColor = MaterialTheme.colorScheme.error
                )
            }
            item {
                HorizontalDivider()
            }
            item {
                SettingsHeader("About")
            }
            item {
                SettingsItem(
                    title = "App Version",
                    subtitle = "1.0.0 (Synopsis Project)",
                    icon = Icons.Default.Info,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        headlineContent = { Text(title, color = textColor) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (textColor == MaterialTheme.colorScheme.error) textColor else MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}
