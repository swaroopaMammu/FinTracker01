package com.example.fintracker.view.screens

import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fintracker.viewmodel.CategoryViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry



@Composable
fun CategoryScreen(navController: NavController,date:String) {
    val viewModel: CategoryViewModel = hiltViewModel()
    var budget by remember { mutableStateOf("") }
    // Fetch data only once when the screen loads
    LaunchedEffect(Unit) {
        viewModel.getTotalSpendByCategoryAndMonth(date)
    }

    val expList by viewModel.totalByCategoryLiveData.collectAsState(initial = emptyList())
    val categoryBudget by viewModel.categoryBudgetLiveData.collectAsState(initial = "")

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
            Text(text = "Budget in Category : $categoryBudget")
            ChartView(
                Modifier
                    .padding(padding)
                    .height(300.dp),expList)

            CategoryDropdown {
                viewModel.updateCategory(it.name)
                viewModel.getExpensesByCategory(date)
                viewModel.getBudgetByCategoryAndMonth(date)
            }

            Card(  modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)) {
                Row (horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()){
                    OutlinedTextField(value = budget, onValueChange = {
                        if (it.matches(Regex("^\\d*\\.?\\d*\$"))) { // Allow numbers & one decimal
                            budget = it
                        }

                    }, placeholder = { Text(text = "Enter budget" )}
                    )
                    IconButton(onClick = { viewModel.insertBudget(date,budget)
                        viewModel.getBudgetByCategoryAndMonth(date)
                        budget = ""
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            }
            ItemListView(Modifier.padding(10.dp), viewModel)
        }
    }
}


@Composable
fun ChartView(modifier:Modifier, expList:List<PieEntry>){

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


@Composable
fun ItemListView(modifier: Modifier,viewModel: CategoryViewModel){
    val expList by  viewModel.expenseByCategoryLiveData.collectAsState(initial = emptyList())
    LazyColumn( modifier = modifier
    ) {
        items(expList.toList())  { item ->
            SingleItemView(item,{})
        }
    }
}

