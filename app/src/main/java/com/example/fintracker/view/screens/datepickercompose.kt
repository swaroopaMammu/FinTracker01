package com.example.fintracker.view.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.fintracker.utils.AppConstants
import com.example.fintracker.utils.getMonthFromDate

@Composable
fun DatePickerBox(modifier: Modifier,label:String, onclick : (date:String,month:String)->Unit) {
    val dateState = remember { mutableStateOf(label) }
    val openDialog = remember { mutableStateOf(false) }

    ConstraintLayout(modifier = modifier) {
        val (titleID,iconId) = createRefs()
        Text(text = "Spend in ${dateState.value}", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.constrainAs(titleID){
            start.linkTo(parent.start)
            end.linkTo(iconId.start)
            top.linkTo(parent.top)
        })
        IconButton(onClick = {
            openDialog.value = true
        },modifier = Modifier.constrainAs(iconId){
            top.linkTo(titleID.top)
            bottom.linkTo(titleID.bottom)
            end.linkTo(parent.end)
        }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = ""
            )
        }
        if (openDialog.value) {
            DatePickerView {
                dateState.value = it.getMonthFromDate()
                openDialog.value = false
                onclick.invoke(it,dateState.value)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(onclick : (data:String)->Unit){
    val dateState = remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()
   Column {
           DatePickerDialog(
               onDismissRequest = { onclick.invoke(dateState.value) },
               confirmButton = {
                   TextButton(onClick = {
                       onclick.invoke(dateState.value)
                   }) {
                       Text(text = "Ok")
                   }
               }
           ) {
               DatePicker(state = datePickerState, showModeToggle = false)
               LaunchedEffect(datePickerState.selectedDateMillis) {
                   datePickerState.selectedDateMillis?.let { millis ->
                       dateState.value = SimpleDateFormat(AppConstants.YYYY_MM_DD, Locale.getDefault()).format(
                           Date(millis)
                       )
                   }
               }
           }
   }
}

