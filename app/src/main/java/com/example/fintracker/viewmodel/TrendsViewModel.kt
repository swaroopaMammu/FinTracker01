package com.example.fintracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintracker.data.repository.ExpenseRepository
import com.example.fintracker.utils.getMonthNumber
import com.example.fintracker.utils.getYearFromDate
import com.github.mikephil.charting.data.BarEntry
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
class TrendsViewModel  @Inject constructor(private val repo: ExpenseRepository) : ViewModel(){

    private val _totalByMonthLiveData = MutableStateFlow<List<BarEntry>>(emptyList())
    val totalByMonthLiveData: StateFlow<List<BarEntry>> = _totalByMonthLiveData.asStateFlow()

    fun getExpenseListPerMonth(date:String) {
        val year = date.getYearFromDate()
        if(year.isNotEmpty()){
            viewModelScope.launch {
                repo.getExpenseListPerMonth(year)
                    .collectLatest { data ->
                        if (data.isNotEmpty()) {
                            val list = data.map {
                                BarEntry( it.month.getMonthNumber().toFloat(),it.total.toFloat())
                            }
                            withContext(Dispatchers.Main) {
                                _totalByMonthLiveData.value = list
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                _totalByMonthLiveData.value = emptyList()
                            }
                        }
                    }
            }
        }
    }
}