package com.raye.fintrack.feature.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.ui.component.DeleteConfirmationDialog
import com.raye.fintrack.core.ui.component.EmptyState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {

    var selectedType by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }

    val categories by viewModel
        .categoriesByType(selectedType)
        .collectAsState(initial = emptyList())

    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (categoryToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Category",
            message = "Are you sure you want to delete '${categoryToDelete?.name}'?",
            onConfirm = {
                categoryToDelete?.let { category ->
                    viewModel.deleteCategory(category)
                    scope.launch {
                        snackbarHostState.showSnackbar("Category deleted")
                    }
                }
            },
            onDismiss = { categoryToDelete = null }
        )
    }

    if (showAddDialog || categoryToEdit != null) {
        AddCategoryDialog(
            categoryToEdit = categoryToEdit,
            onDismiss = {
                showAddDialog = false
                categoryToEdit = null
            },
            onConfirm = { name, type ->
                if (categoryToEdit != null) {
                    viewModel.updateCategory(categoryToEdit!!.copy(name = name, type = type))
                    scope.launch {
                        snackbarHostState.showSnackbar("Category updated")
                    }
                } else {
                    viewModel.addCategory(Category(name = name, type = type))
                    scope.launch {
                        snackbarHostState.showSnackbar("Category added")
                    }
                }
                showAddDialog = false
                categoryToEdit = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Categories")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->


        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {


            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(
                    selected = selectedType == TransactionType.EXPENSE,
                    onClick = {
                        selectedType = TransactionType.EXPENSE
                    },
                    label = {
                        Text("Expense")
                    }
                )


                FilterChip(
                    selected = selectedType == TransactionType.INCOME,
                    onClick = {
                        selectedType = TransactionType.INCOME
                    },
                    label = {
                        Text("Income")
                    }
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (categories.isEmpty()) {
                EmptyState(
                    message = "No categories found for this type.",
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    items(categories) { category ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.name,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            categoryToEdit = category
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }

                                    if (!category.isDefault) {
                                        IconButton(
                                            onClick = {
                                                categoryToDelete = category
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete ${category.name}"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
