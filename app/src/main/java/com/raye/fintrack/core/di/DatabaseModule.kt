package com.raye.fintrack.core.di

import android.content.Context
import androidx.room.Room
import com.raye.fintrack.core.database.FinTrackDatabase
import com.raye.fintrack.core.database.dao.AccountDao
import com.raye.fintrack.core.database.dao.CategoryDao
import com.raye.fintrack.core.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FinTrackDatabase {
        return Room.databaseBuilder(
            context,
            FinTrackDatabase::class.java,
            "fintrack.db"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideAccountDao(database: FinTrackDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideCategoryDao(database: FinTrackDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: FinTrackDatabase): TransactionDao = database.transactionDao()
}
