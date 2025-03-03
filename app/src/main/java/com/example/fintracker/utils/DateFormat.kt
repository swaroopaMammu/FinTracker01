package com.example.fintracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar


fun String.getMonthFromDate(): String {
    if(this.isEmpty()){
        return this
    }
    val inputFormat = SimpleDateFormat("yyyy_MM_dd", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMMM", Locale.ENGLISH) // Full month name

    val date: Date = inputFormat.parse(this)!!
    return outputFormat.format(date)
}

fun getCurrentMonth(): String {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("MMMM", Locale.ENGLISH)
    return formatter.format(calendar.time)
}

fun getCurrentYearMonth(): String {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy_MM", Locale.ENGLISH)
    return formatter.format(calendar.time)
}

fun String.formatDateMonth(): String {
    val inputFormat = SimpleDateFormat(AppConstants.YYYY_MM_DD, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)

    val date: Date = inputFormat.parse(this)!!
    return outputFormat.format(date)
}

fun String.getMonthNumber(): Int {
    if(this.isEmpty()){
        return 0
    }
    val sdf = SimpleDateFormat("yyyy_MM", Locale.getDefault())
    val date = sdf.parse(this)
    return date?.month?.plus(1) ?: -1 // `month` is 0-based, so add 1
}

fun String.getYearFromDate(): String {
    if(this.isEmpty()){
        return ""
    }
    val sdf = SimpleDateFormat("yyyy_MM", Locale.getDefault())
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    return calendar.get(Calendar.YEAR).toString()
}
