package com.example.fintracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.data.repository.ExpenseRepository
import com.example.fintracker.utils.formatYearMonth
import com.example.fintracker.utils.getCurrentMonth
import com.example.fintracker.utils.getCurrentYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: ExpenseRepository) : ViewModel() {

    private val _monthLiveData = MutableLiveData(getCurrentMonth())
    val monthLiveData: LiveData<String> = _monthLiveData

    private val _totalSpendLiveData = MutableLiveData(0L)
    val totalSpendLiveData: LiveData<Long> = _totalSpendLiveData

    private val _dateLiveData = MutableLiveData(getCurrentYearMonth())
    val dateLiveData: LiveData<String> = _dateLiveData

    private val _expenseByMonthLiveData = MutableStateFlow<List<ExpenseModel>>(emptyList())
    val expenseByMonthLiveData: StateFlow<List<ExpenseModel>> = _expenseByMonthLiveData.asStateFlow()

    private val _monthlyBudgetLiveData = MutableStateFlow("")
    val monthlyBudgetLiveData: StateFlow<String> = _monthlyBudgetLiveData.asStateFlow()


    init {
        getTotalBudgetByMonthYear()
    }

    fun updateMonth(date:String){
        _monthLiveData.value = date
    }

    fun updateDate(date:String){
        _dateLiveData.value = date
    }

    fun getTotalSpendByMonth(){
        viewModelScope.launch {
            dateLiveData.value?.let {
                val date = it.formatYearMonth()
                if(date.isNotEmpty()){
                    repo.getTotalSpendByMonth(date)?.collectLatest { data ->
                        _totalSpendLiveData.value = data
                    }
                }
            }
        }
    }

    fun getExpensesByDate(){
        viewModelScope.launch {
            dateLiveData.value?.let {
                val date = it.formatYearMonth()
               if(date.isNotEmpty()){
                   repo.getExpensesByDate(date).collectLatest { data ->
                       _expenseByMonthLiveData.value = data
                   }
               }
            }
        }
    }

     fun getTotalBudgetByMonthYear(){
         viewModelScope.launch {
             dateLiveData.value?.let {
                 val date = it.formatYearMonth()
                 if(date.isNotEmpty()) {
                     repo.getTotalBudgetByMonthYear(date)?.collectLatest { data ->
                         _monthlyBudgetLiveData.value = data.toString()
                     }
                 }
             }
         }
    }

}