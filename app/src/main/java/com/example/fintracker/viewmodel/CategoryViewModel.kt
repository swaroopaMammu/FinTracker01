package com.example.fintracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintracker.data.db.entity.BudgetModel
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.data.repository.ExpenseRepository
import com.example.fintracker.utils.formatDateMonth
import com.example.fintracker.utils.formatYearMonth
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repo: ExpenseRepository) : ViewModel() {
    private val _categoryLiveData = MutableLiveData("Category")
    val categoryLiveData: LiveData<String> = _categoryLiveData

    private val _expenseByCategoryLiveData = MutableStateFlow<List<ExpenseModel>>(emptyList())
    val expenseByCategoryLiveData: StateFlow<List<ExpenseModel>> = _expenseByCategoryLiveData.asStateFlow()

    private val _totalByCategoryLiveData = MutableStateFlow<List<PieEntry>>(emptyList())
    val totalByCategoryLiveData: StateFlow<List<PieEntry>> = _totalByCategoryLiveData.asStateFlow()

    private val _categoryBudgetLiveData = MutableStateFlow("")
    val categoryBudgetLiveData: StateFlow<String> = _categoryBudgetLiveData.asStateFlow()

    fun updateCategory(category: String) {
        if (_categoryLiveData.value != category) {
            _categoryLiveData.value = category
        }
    }
    fun getExpensesByCategory(date:String){
        viewModelScope.launch {
            categoryLiveData.value?.let {
                val d = date.formatYearMonth()
                if(it != "Category" && d.isNotEmpty()){
                    repo.getExpensesByCategory(it,d).collectLatest { data ->
                        _expenseByCategoryLiveData.value = data
                    }
                }
            }
        }
    }

    fun  insertBudget(date:String,amount:String){
        viewModelScope.launch {
            categoryLiveData.value?.let {
                if(it != "Category" && amount.isNotEmpty()){
                    val budgetData = BudgetModel(
                        category = it,
                        monthlyBudget = amount.toLong(),
                        monthYear = date
                    )
                    repo.insertBudget(budgetData)
                }
            }
        }
    }

    fun getTotalSpendByCategoryAndMonth(date:String) {
        viewModelScope.launch {
            val d = date.formatYearMonth()
         if(d.isNotEmpty()){
             repo.getTotalSpendByCategoryAndMonth(d)
                 .collectLatest { data ->
                     if (data.isNotEmpty()) {
                         val list = data.map { PieEntry(it.total.toFloat(), it.category) }
                         withContext(Dispatchers.Main) {
                             _totalByCategoryLiveData.value = list
                         }
                     } else {
                         withContext(Dispatchers.Main) {
                             _totalByCategoryLiveData.value = emptyList()
                         }
                     }
                 }
         }
        }
    }

    fun getBudgetByCategoryAndMonth(date:String) {
        viewModelScope.launch {
            categoryLiveData.value?.let {
                val d = date.formatYearMonth()
                if(it != "Category" && d.isNotEmpty()){
                    repo.getBudgetByCategoryAndMonth(d,it).collectLatest { d ->
                        _categoryBudgetLiveData.value = d.toString()
                    }
                }
            }
        }
    }


}