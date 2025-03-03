package com.example.fintracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEntryViewModel @Inject constructor(private val repo: ExpenseRepository) : ViewModel() {

    private val _dateLiveData = MutableLiveData("Date")
    val dateLiveData: LiveData<String> = _dateLiveData
    private val _categoryLiveData = MutableLiveData("Category")
    val categoryLiveData: LiveData<String> = _categoryLiveData
    private val _notesLiveData = MutableLiveData("")
    val notesLiveData: LiveData<String> = _notesLiveData
    private val _amountLiveData = MutableLiveData("")
    val amountLiveData: LiveData<String> = _amountLiveData
    private val _titleLiveData = MutableLiveData("")
    val titleLiveData : LiveData<String> = _titleLiveData



    private fun validateFields():Boolean{
        return  titleLiveData.value?.isNotEmpty() == true && amountLiveData.value != "" &&
                categoryLiveData.value != "Category" &&  dateLiveData.value != "Date"
    }


    fun reSetAllNEwEntryFields(){
        _dateLiveData.value = "Date"
        _categoryLiveData.value = "Category"
        _notesLiveData.value = ""
        _dateLiveData.value = "Date"
        _titleLiveData.value = ""
        _amountLiveData.value = ""
    }

    fun updateTitle(date:String){
        _titleLiveData.value = date
    }


    fun updateDate(date:String){
        _dateLiveData.value = date
    }

    fun updateCategory(category:String){
        _categoryLiveData.value = category
    }

    fun updateNote(note:String){
        _notesLiveData.value = note
    }

    fun updateAmount(amount:String){
        _amountLiveData.value = amount
    }

    fun insertNewExpense(isNew:Boolean=true,expId: Int?=null){
        if(validateFields()){
            val data = ExpenseModel(
                title = titleLiveData.value?:"",
                category = categoryLiveData.value?:"",
                date = dateLiveData.value?:"",
                amount = amountLiveData.value?.toLong()?:0L,
                notes = notesLiveData.value?:"",
                expId = expId?:0
            )
            if(isNew){
                viewModelScope.launch {
                    repo.insertNewExpense(data)
                }
            }else{
                updateMonthlyTable(data)
            }
            reSetAllNEwEntryFields()
        }
    }

    private fun updateMonthlyTable(data:ExpenseModel){
        viewModelScope.launch {
            repo.updateMonthlyTable(data)
        }
    }

    fun deleteExpense(id:String){
        viewModelScope.launch {
            repo.deleteExpense(id.toInt())
        }
    }

    fun getExpensesDataById(id:Int?){
        id?.let {
            viewModelScope.launch {
                repo.getExpensesDataById(it).collectLatest { data ->
                    updateTitle(data.title)
                    updateAmount(data.amount.toString())
                    updateDate(data.date)
                    updateNote(data.notes)
                    updateCategory(data.category)
                }
            }
        }
    }
}