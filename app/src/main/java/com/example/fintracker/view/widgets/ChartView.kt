package com.example.fintracker.view.widgets

import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun ChartView(modifier: Modifier, expList:List<PieEntry>){

    AndroidView(
        factory = { context ->
            val pieChart = PieChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                setUsePercentValues(true)
                setEntryLabelTextSize(12f)
                setEntryLabelColor(Color.BLACK)
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.isWordWrapEnabled = true
                animateY(1000)
            }
            if(pieChart.isEmpty){
                pieChart.clear()
            }
            pieChart
        },
        update = { pieChart ->

            if (expList.isEmpty()) {
                pieChart.clear()
                pieChart.invalidate()
                return@AndroidView
            }

            val colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA)

            val pieDataSet = PieDataSet(expList, "Expense Categories").apply {
                setColors(colors)
                valueTextSize = 14f
                valueTypeface = Typeface.DEFAULT_BOLD
            }

            val pieData = PieData(pieDataSet)
            pieChart.data = pieData
            pieChart.invalidate()
        },
        modifier = modifier
    )
}