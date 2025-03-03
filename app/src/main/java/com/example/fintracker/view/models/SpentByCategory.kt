package com.example.fintracker.view.models

data class SpentByCategory(
    val category: String,
    val total: Long
)

data class SpentByMonth(
    val month: String,
    val total: Long
)