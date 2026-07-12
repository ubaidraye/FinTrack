package com.raye.fintrack.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType
import com.raye.fintrack.core.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    val categories = repository.categories

    fun categoriesByType(type: TransactionType): Flow<List<Category>> {
        return repository.getCategoriesByType(type)
    }

    fun add(category: Category) {
        viewModelScope.launch {
            repository.add(category)
        }
    }

    fun update(category: Category) {
        viewModelScope.launch {
            repository.update(category)
        }
    }

    fun delete(category: Category) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }
}