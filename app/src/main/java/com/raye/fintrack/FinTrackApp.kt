package com.raye.fintrack

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raye.fintrack.core.database.DatabaseViewModel
import com.raye.fintrack.core.database.entity.Account
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.navigation.NavGraph
import com.raye.fintrack.core.navigation.Routes
import com.raye.fintrack.core.ui.component.BottomBar
import com.raye.fintrack.feature.account.AccountViewModel
import com.raye.fintrack.feature.account.AddAccountDialog
import com.raye.fintrack.feature.category.AddCategoryDialog
import com.raye.fintrack.feature.category.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinTrackApp() {

    val databaseViewModel: DatabaseViewModel = hiltViewModel()
    val accountViewModel: AccountViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var showAddAccountDialog by remember {
        mutableStateOf(false)
    }
    var showAddCategoryDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        floatingActionButton = {

            when (currentRoute) {

                Routes.DASHBOARD,

                Routes.TRANSACTIONS -> {

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(
                                Routes.ADD_TRANSACTION
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Transaction"
                        )
                    }
                }

                Routes.CATEGORIES -> {
                    FloatingActionButton(
                        onClick = {
                            showAddCategoryDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Category"
                        )
                    }
                }

                Routes.ACCOUNTS -> {

                    FloatingActionButton(
                        onClick = {
                            showAddAccountDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Account"
                        )
                    }
                }


                else -> {
                    // No FAB
                }
            }
        }
    ) { paddingValues ->

        NavGraph(
            navController = navController,
            paddingValues = paddingValues
        )


        if (showAddAccountDialog) {

            AddAccountDialog(
                onDismiss = {
                    showAddAccountDialog = false
                },
                onConfirm = { name ->

                    accountViewModel.addAccount(
                        Account(
                            name = name,
                            icon = "default"
                        )
                    )

                    showAddAccountDialog = false
                }
            )
        }

        if (showAddCategoryDialog) {

            AddCategoryDialog(
                onDismiss = {
                    showAddCategoryDialog = false
                },
                onConfirm = { name, type ->

                    categoryViewModel.addCategory(
                        Category(
                            name = name,
                            type = type,
                            isDefault = false
                        )
                    )

                    showAddCategoryDialog = false
                }
            )
        }
    }
}