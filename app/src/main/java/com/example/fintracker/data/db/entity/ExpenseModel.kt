package com.example.fintracker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseModel(
    @PrimaryKey(autoGenerate = true)
    val expId:Int = 0,
    val amount:Long,
    val title:String,
    val date:String,
    val category:String,
    val notes:String
)