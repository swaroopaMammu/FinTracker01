package com.example.fintracker.data.di

import android.content.Context
import com.example.fintracker.data.db.AppDatabase
import com.example.fintracker.data.db.dao.BudgetDao
import com.example.fintracker.data.db.dao.ExpenseDao
import com.example.fintracker.data.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FinTrackerModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context):AppDatabase{
        return AppDatabase.getInstance(context = context)
    }

    @Provides
    @Singleton
    fun getBudgetDao(db: AppDatabase): BudgetDao{
        return db.bdtDao()
    }
    @Provides
    @Singleton
    fun getExpenseDao(db: AppDatabase): ExpenseDao{
        return db.expDao()
    }

    @Provides
    @Singleton
    fun getExpenseRepo(bgtDob: BudgetDao,expDao:ExpenseDao): ExpenseRepository{
        return ExpenseRepository(expDao,bgtDob)
    }

}