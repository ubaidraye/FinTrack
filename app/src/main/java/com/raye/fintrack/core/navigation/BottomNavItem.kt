package com.raye.fintrack.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    data object Dashboard : BottomNavItem(
        Routes.DASHBOARD,
        "Dashboard",
        Icons.Default.Home
    )

    data object Transactions : BottomNavItem(
        Routes.TRANSACTIONS,
        "Transactions",
        Icons.AutoMirrored.Filled.List
    )

    data object Reports : BottomNavItem(
        Routes.REPORTS,
        "Reports",
        Icons.Default.BarChart
    )

    data object Accounts : BottomNavItem(
        Routes.ACCOUNTS,
        "Accounts",
        Icons.Default.AccountBalance
    )

    data object Categories : BottomNavItem(
        Routes.CATEGORIES,
        "Categories",
        Icons.Default.Category
    )

    data object Settings : BottomNavItem(
        Routes.SETTINGS,
        "Settings",
        Icons.Default.Settings
    )
}