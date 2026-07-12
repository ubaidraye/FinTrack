package com.raye.fintrack.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.repository.CategoryRepository
import com.raye.fintrack.core.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {


    val categories = repository.categories


    fun categoriesByType(
        type: TransactionType
    ): Flow<List<Category>> {

        return repository.getCategoriesByType(type)
    }


    fun addCategory(
        category: Category
    ) {

        viewModelScope.launch {
            repository.add(category)
        }

    }


    fun deleteCategory(category: Category) {

        viewModelScope.launch {

            if (category.isDefault) {
                return@launch
            }


            val count = transactionRepository
                .countByCategory(category.id)


            if (count == 0) {
                repository.delete(category)
            }

        }
    }
}