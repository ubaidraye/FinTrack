package com.raye.fintrack.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.raye.fintrack.feature.account.AccountScreen
import com.raye.fintrack.feature.analysis.AnalysisScreen
import com.raye.fintrack.feature.category.CategoryScreen
import com.raye.fintrack.feature.dashboard.DashboardScreen
import com.raye.fintrack.feature.settings.SettingsScreen
import com.raye.fintrack.feature.transaction.AddTransactionScreen
import com.raye.fintrack.feature.transaction.TransactionDetailScreen
import com.raye.fintrack.feature.transaction.TransactionScreen
import com.raye.fintrack.feature.transaction.TransactionViewModel

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
            DashboardScreen(
                onTransactionClick = { transaction ->
                    navController.navigate("${Routes.TRANSACTION_DETAIL}/${transaction.id}")
                }
            )
        }

        composable(Routes.TRANSACTIONS) {
            TransactionScreen(
                onTransactionClick = { transaction ->
                    navController.navigate("${Routes.TRANSACTION_DETAIL}/${transaction.id}")
                }
            )
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

        composable(Routes.ANALYSIS) {
            AnalysisScreen()
        }

        composable(
            route = "${Routes.ADD_TRANSACTION}?transactionId={transactionId}",
            arguments = listOf(
                androidx.navigation.navArgument("transactionId") {
                    type = androidx.navigation.NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            val viewModel: TransactionViewModel = hiltViewModel()
            val transactions by viewModel.transactions.collectAsState(initial = emptyList())
            val transactionToEdit = transactions.find { it.transaction.id == transactionId }?.transaction

            AddTransactionScreen(
                transactionToEdit = transactionToEdit,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.TRANSACTION_DETAIL}/{transactionId}",
            arguments = listOf(
                androidx.navigation.navArgument("transactionId") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            TransactionDetailScreen(
                transactionId = transactionId,
                onEdit = { transaction ->
                    navController.navigate("${Routes.ADD_TRANSACTION}?transactionId=${transaction.id}")
                },
                onDelete = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
