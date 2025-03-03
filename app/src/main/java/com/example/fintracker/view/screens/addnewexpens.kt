package com.example.fintracker.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fintracker.utils.CategoryLabels
import com.example.fintracker.view.widgets.CategoryDropdown
import com.example.fintracker.viewmodel.NewEntryViewModel

@Composable
fun AddNewExpense(navController: NavController, expId:String){
    val viewModel:NewEntryViewModel = hiltViewModel()
    val newEntry = expId.isEmpty()
    val id = expId.toIntOrNull()
    LaunchedEffect(Unit) {
        if (!newEntry) {
            viewModel.getExpensesDataById(id)
        }
    }
    Scaffold{ padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxWidth()) {
            IconButton(onClick = {
               viewModel.reSetAllNEwEntryFields()
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
            AmountCardView(viewModel)
            Button( modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), shape = RoundedCornerShape(10.dp),
                onClick = {
                    if(newEntry){
                        viewModel.insertNewExpense()
                    }else{
                        viewModel.deleteExpense(id)
                    }
                    navController.popBackStack()
                          },
                colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                Text(text = if(newEntry)"Save Entry" else "Delete Entry", color = Color.White)
            }
            if(!newEntry){
                Button( modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), shape = RoundedCornerShape(10.dp),
                    onClick = {
                        viewModel.insertNewExpense(false,expId.toIntOrNull())
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                    Text(text = "Update Data", color = Color.White)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountCardView(viewModel:NewEntryViewModel){
    val amountState = viewModel.amountLiveData.observeAsState("")
    val categoryState = viewModel.categoryLiveData.observeAsState("Category")
    val noteState = viewModel.notesLiveData.observeAsState("")
    val dateState = viewModel.dateLiveData.observeAsState("Date")
    val titleState = viewModel.titleLiveData.observeAsState("")
    var opendialog by remember { mutableStateOf(false) }
    val state = rememberDatePickerState()
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)) {
       Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)){
           Text(text = "Amount spent")
           TextField(
               modifier = Modifier.width(160.dp),
               value = amountState.value,
               onValueChange = {
                   if (it.matches(Regex("^\\d*\\.?\\d*\$"))) { // Allow numbers & one decimal
                       viewModel.updateAmount(it)
                   }
               },
               placeholder = {
                   Text(text = "Enter amount")
               }
           )
           Spacer(modifier = Modifier.height(10.dp))
           Card(  modifier = Modifier
               .padding(start = 20.dp, end = 20.dp, top = 10.dp)
               .fillMaxWidth()
               .wrapContentHeight(),
               elevation = CardDefaults.cardElevation(8.dp),
               shape = RoundedCornerShape(12.dp),
               colors = CardDefaults.cardColors(containerColor = Color.DarkGray)){
               Column(modifier = Modifier
                   .fillMaxWidth()
                   .padding(start = 16.dp, top = 10.dp)) {
                   TextField(value = titleState.value,
                       placeholder = { Text(text = "Enter title", color = Color.Gray)},
                       onValueChange ={  viewModel.updateTitle(it)},
                       maxLines = 5,
                       colors = TextFieldDefaults.colors(
                           focusedIndicatorColor = Color.Transparent,
                           unfocusedIndicatorColor = Color.Transparent,
                           disabledIndicatorColor = Color.Transparent,
                           focusedContainerColor = Color.Transparent,
                           cursorColor = Color.White,
                           unfocusedContainerColor = Color.Transparent,
                           focusedTextColor = Color.White,
                           unfocusedTextColor = Color.White
                       )
                   )
               }
           }
          Card(  modifier = Modifier
              .padding(start = 20.dp, top = 20.dp, end = 20.dp)
              .fillMaxWidth()
              .wrapContentHeight(),
              elevation = CardDefaults.cardElevation(8.dp),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color.DarkGray)){
              Row(modifier = Modifier
                  .fillMaxWidth()
                  .padding(start = 16.dp), horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                 Text(text = dateState.value, color = Color.White)
                 IconButton(onClick = { opendialog = true }) {
                     Icon(imageVector = Icons.Default.DateRange, contentDescription = "", tint = Color.White )
                 }
              }
              if(opendialog){
                  DatePickerView(state) { viewModel.updateDate(it)
                      opendialog = false
                  }
              }
          }

           CategoryDropdown(categoryState.value){ viewModel.updateCategory(it.name) }
           Card(  modifier = Modifier
               .padding(start = 20.dp, end = 20.dp, top = 10.dp)
               .fillMaxWidth()
               .wrapContentHeight(),
               elevation = CardDefaults.cardElevation(8.dp),
               shape = RoundedCornerShape(12.dp),
               colors = CardDefaults.cardColors(containerColor = Color.DarkGray)){
               Column(modifier = Modifier
                   .fillMaxWidth()
                   .padding(start = 16.dp, top = 10.dp)) {
                   Text(text = "Notes", color = Color.White)
                   TextField(value = noteState.value,
                       placeholder = { Text(text = "Enter notes", color = Color.Gray)},
                       onValueChange ={  viewModel.updateNote(it)},
                       maxLines = 5,
                       colors = TextFieldDefaults.colors(
                           focusedIndicatorColor = Color.Transparent,
                           unfocusedIndicatorColor = Color.Transparent,
                           disabledIndicatorColor = Color.Transparent,
                           focusedContainerColor = Color.Transparent,
                           unfocusedContainerColor = Color.Transparent,
                           focusedTextColor = Color.White,
                           unfocusedTextColor = Color.White,
                           cursorColor = Color.White,
                       )
                   )
               }
           }

       }
    }
}
