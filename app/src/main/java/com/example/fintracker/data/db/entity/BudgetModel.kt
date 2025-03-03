package com.example.fintracker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class BudgetModel(
    @PrimaryKey(autoGenerate = true)
    val budgetId:Int = 0,
    val category:String,
    val monthlyBudget : Long,
    val monthYear:String
)
