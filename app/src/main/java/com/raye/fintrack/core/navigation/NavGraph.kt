package com.raye.fintrack.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raye.fintrack.feature.account.AccountScreen
import com.raye.fintrack.feature.category.CategoryScreen
import com.raye.fintrack.feature.dashboard.DashboardScreen
import com.raye.fintrack.feature.settings.SettingsScreen
import com.raye.fintrack.feature.transaction.AddTransactionScreen
import com.raye.fintrack.feature.transaction.TransactionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
    ) {

        composable(Routes.DASHBOARD) {
            DashboardScreen()
        }

        composable(Routes.TRANSACTIONS) {
            TransactionScreen()
        }

        composable(Routes.ACCOUNTS) {
            AccountScreen()
        }

        composable(Routes.CATEGORIES) {
            CategoryScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }
        composable(Routes.ADD_TRANSACTION) {

            AddTransactionScreen(
                onBack = {
                    navController.popBackStack()
                }
            )

        }
    }
}