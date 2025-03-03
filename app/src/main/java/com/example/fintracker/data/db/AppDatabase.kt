package com.example.fintracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fintracker.data.db.dao.BudgetDao
import com.example.fintracker.data.db.dao.ExpenseDao
import com.example.fintracker.data.db.entity.BudgetModel
import com.example.fintracker.data.db.entity.ExpenseModel


@Database(entities = [ExpenseModel::class,BudgetModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun expDao():ExpenseDao
    abstract fun bdtDao(): BudgetDao

    companion object{
        private var Instance:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase{
            synchronized(this){
                var instance = Instance
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "fin_tracker_db").build()
                }
                Instance = instance
                return instance
            }
        }
    }
}