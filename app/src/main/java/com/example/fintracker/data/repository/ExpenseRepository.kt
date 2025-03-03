package com.example.fintracker.data.repository

import com.example.fintracker.data.db.dao.BudgetDao
import com.example.fintracker.data.db.dao.ExpenseDao
import com.example.fintracker.data.db.entity.BudgetModel
import com.example.fintracker.data.db.entity.ExpenseModel
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseDao: ExpenseDao,private val budgetDao:BudgetDao) {

  suspend fun  insertBudget(budgetModel: BudgetModel){
      budgetDao.insertBudget(budgetModel)
  }
  fun getTotalBudgetByMonthYear(yearMonth: String) = budgetDao.getTotalBudgetByMonthYear(yearMonth)

  fun getBudgetByCategoryAndMonth(yearMonth: String,category:String) = budgetDao.getBudgetByCategoryAndMonth(yearMonth,category)


    suspend fun insertNewExpense(expenseModel: ExpenseModel){
        expenseDao.insertNewExpense(expenseModel)
    }

    suspend fun deleteExpense(id: Int){
        expenseDao.deleteExpenseById(id)
    }
    fun getExpensesDataById(id: Int) = expenseDao.getExpensesDataById(id)


    fun getExpensesByDate(date: String) = expenseDao.getExpensesByDate(date)

    fun getExpensesByCategory(category: String,date:String) = expenseDao.getExpensesByCategory(category,date)

    fun getTotalSpendByMonth(date:String) = expenseDao.getTotalSpendByMonth(date)

    fun getTotalSpendByCategoryAndMonth(date:String) = expenseDao.getTotalSpendByCategoryAndMonth(date)

    fun getExpenseListPerMonth(date:String) = expenseDao.getExpenseListPerMonth(date)
}