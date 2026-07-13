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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raye.fintrack.core.model.TransactionType

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Categories")
                }
            )
        }
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


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(categories) { category ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {
                            Column {
                                Text(
                                    text = category.name,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            if (!category.isDefault){
                                IconButton(
                                    onClick = {
                                        viewModel.deleteCategory(category)
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