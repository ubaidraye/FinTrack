package com.raye.fintrack.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raye.fintrack.core.database.entity.Category
import com.raye.fintrack.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT COUNT(*) FROM category")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM category ORDER BY name ASC")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE type = :type ORDER BY name ASC")
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM category
            WHERE LOWER(name) = LOWER(:name)
            AND type = :type
        )
    """
    )
    suspend fun exists(name: String, type: TransactionType): Boolean
}