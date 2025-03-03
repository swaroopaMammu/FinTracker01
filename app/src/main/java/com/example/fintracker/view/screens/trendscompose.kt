package com.example.fintracker.view.screens

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fintracker.viewmodel.TrendsViewModel
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun TrendsScreen(navController: NavController, date: String) {
    val viewModel: TrendsViewModel = hiltViewModel()

    // Fetch data when date changes
    LaunchedEffect(date) {
        viewModel.getExpenseListPerMonth(date)
    }

    val expList by viewModel.totalByMonthLiveData.collectAsState(initial = emptyList())

    Scaffold { padding ->
        BarChartView(
            modifier = Modifier.padding(padding),
            list = expList
        )
    }
}

@Composable
fun BarChartView(modifier: Modifier, list: List<BarEntry>) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                setDrawGridBackground(false)
                setDrawBarShadow(false)
                setPinchZoom(false)
                setDrawValueAboveBar(true)
                legend.isEnabled = false
                animateY(1000)  // Smooth animation
            }
        },
        update = { barChart ->
            if (list.isEmpty()) {
                barChart.clear()
                barChart.invalidate()
                return@AndroidView
            }

            val barDataSet = BarDataSet(list, "Monthly Expenses").apply {
                color = Color.GREEN
                valueTextSize = 12f
                valueTypeface = Typeface.DEFAULT_BOLD
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "$${value.toInt()}"
                    }
                }
            }

            val barData = BarData(barDataSet).apply {
                barWidth = 0.1f
            }
            barChart.data = barData

            val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                setDrawAxisLine(true)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return months.getOrNull(value.toInt()-1) ?: value.toInt().toString()
                    }
                }
                granularity = 1f
            }
            barChart.axisLeft.apply {
                axisMinimum = 0f // Start from 0
                axisMaximum = 20000f // Adjust based on your max budget
                granularity = 1000f // Step size (1000, 2000, 3000, etc.)
                setDrawGridLines(true)
                setDrawLabels(true) // Show Y-axis labels
            }


          //  barChart.axisLeft.setDrawGridLines(false)
            barChart.axisRight.isEnabled = false

            barChart.invalidate()  // Refresh chart
        },
        modifier = modifier
    )
}

