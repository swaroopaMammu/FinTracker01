package com.example.fintracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.view.models.SpentByCategory
import com.example.fintracker.view.models.SpentByMonth
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewExpense(expenseModel: ExpenseModel)


    @Query("DELETE FROM expense_table WHERE expId = :id")
    suspend fun deleteExpenseById(id: Int)

    @Query("SELECT * FROM expense_table WHERE expId = :id")
    fun getExpensesDataById(id:Int): Flow<ExpenseModel?>

    @Query("SELECT * FROM expense_table WHERE date LIKE :date || '%'")
    fun getExpensesByDate(date:String):Flow<List<ExpenseModel>>

    @Query("SELECT * FROM expense_table WHERE category LIKE :category AND date LIKE :date || '%'")
    fun getExpensesByCategory(category: String,date:String):Flow<List<ExpenseModel>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expense_table WHERE date LIKE :yearMonth || '%'")
    fun getTotalSpendByMonth(yearMonth: String): Flow<Long>?

    @Query(
        "SELECT category, COALESCE(SUM(amount), 0) as total " +
                "FROM expense_table " +
                "WHERE date LIKE :yearMonth || '%' " +
                "GROUP BY category"
    )
    fun getTotalSpendByCategoryAndMonth(yearMonth: String): Flow<List<SpentByCategory>>

    @Query(
        "SELECT SUBSTR(date, 1, 7) AS month, COALESCE(SUM(amount), 0) as total " +
                "FROM expense_table " +
                "WHERE date LIKE :year || '_%' " +
                "GROUP BY month"
    )
    fun getExpenseListPerMonth(year: String): Flow<List<SpentByMonth>>

}