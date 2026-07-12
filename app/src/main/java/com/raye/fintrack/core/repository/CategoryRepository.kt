package com.raye.fintrack.core.repository

import com.raye.fintrack.core.database.dao.CategoryDao
import com.raye.fintrack.core.database.dao.TransactionDao
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val dao: CategoryDao,
    private val transactionDao: TransactionDao
) {

    val categories: Flow<List<Category>> = dao.getCategories()

    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return dao.getCategoriesByType(type)
    }

    suspend fun getCategory(id: Int) = dao.getCategoryById(id)

    suspend fun add(category: Category) = dao.insert(category)

    suspend fun update(category: Category) = dao.update(category)

    suspend fun delete(category: Category) = dao.delete(category)

    suspend fun exists(name: String, type: TransactionType) =
        dao.exists(name, type)

    suspend fun countTransactions(categoryId: Int) =
        transactionDao.countByCategory(categoryId)
}