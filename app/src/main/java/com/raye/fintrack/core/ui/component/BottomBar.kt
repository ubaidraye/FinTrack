package com.raye.fintrack.core.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.raye.fintrack.core.navigation.BottomNavItem

@Composable
fun BottomBar(
    navController: NavController,
) {

    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Transactions,
        BottomNavItem.Analysis,
        BottomNavItem.Categories,
        BottomNavItem.Accounts,
        BottomNavItem.Settings,
    )

    NavigationBar {

        val currentRoute =
            navController.currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(BottomNavItem.Dashboard.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        maxLines = 1,
                        softWrap = false,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}