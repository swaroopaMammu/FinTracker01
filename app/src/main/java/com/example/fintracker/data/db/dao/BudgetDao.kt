package com.example.fintracker.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fintracker.data.db.entity.BudgetModel
import com.example.fintracker.view.models.SpentByCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budgetModel: BudgetModel)

    @Update
    suspend fun updateBudget(budgetModel: BudgetModel)

    @Delete
    suspend fun deleteBudget(budgetModel: BudgetModel)

    @Query("SELECT * FROM budget_table WHERE category = :category AND monthYear = :date" )
    fun getBudgetByCategory(category: String,date:String):BudgetModel

    @Query("SELECT * FROM budget_table")
    fun getAllBudget():LiveData<List<BudgetModel>>

    @Query("SELECT COALESCE(SUM(monthlyBudget), 0) FROM budget_table WHERE monthYear LIKE :yearMonth || '%'")
    fun getTotalBudgetByMonthYear(yearMonth: String): Flow<Long>

    @Query(
        "SELECT COALESCE(SUM(amount), 0) as total " +
                "FROM expense_table " +
                "WHERE date LIKE :yearMonth || '%' " +
                "AND category = :category"
    )
    fun getBudgetByCategoryAndMonth(yearMonth: String,category: String): Flow<Long>


}