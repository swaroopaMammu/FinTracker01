package com.example.fintracker.view.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fintracker.R
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.utils.AppConstants
import com.example.fintracker.utils.formatDateMonth
import com.example.fintracker.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController:NavController){
    val viewModel:HomeViewModel = hiltViewModel()
    val dateState = viewModel.monthLiveData.observeAsState("")
    val monthYear = viewModel.dateLiveData.observeAsState("")
    val totalSpendState = viewModel.totalSpendLiveData.observeAsState("")
    val totalBudget  by  viewModel.monthlyBudgetLiveData.collectAsState(initial = "")
    viewModel.getExpensesByDate()
    viewModel.getTotalSpendByMonth()
    viewModel.getTotalBudgetByMonthYear()
    Scaffold { padding ->
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            val (dateId,expenseCardId,budgetCardId,dividerId,listId,trendsBtnId,categoryBtnId,addNewBtnId,titleId) = createRefs()
            DatePickerBox(modifier = Modifier
                .constrainAs(dateId) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
                .padding(top = 20.dp), onclick = { date,month ->
                viewModel.updateDate(date)
                viewModel.updateMonth(month)
            }, label = dateState.value)
            TopCardView(modifier = Modifier.constrainAs(expenseCardId){
                top.linkTo(dateId.bottom)
                start.linkTo(parent.start)
                end.linkTo(budgetCardId.start)
                width = Dimension.fillToConstraints
            }, title = "Total Spend", amount = "₹${totalSpendState.value}")
            TopCardView(modifier = Modifier
                .constrainAs(budgetCardId) {
                    top.linkTo(dateId.bottom)
                    start.linkTo(expenseCardId.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(end = 10.dp), title = "Total Budget", amount = "₹$totalBudget")

            Box(modifier = Modifier
                .constrainAs(dividerId) {
                    top.linkTo(expenseCardId.bottom, margin = 40.dp)
                }
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray))
            Button(onClick = { navController.navigate("${AppConstants.TRENDS}/${monthYear.value}")}, modifier = Modifier.constrainAs(trendsBtnId){
                top.linkTo(dividerId.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(categoryBtnId.start)
            }) {
                Icon(painter = painterResource(id = R.drawable.icon_bar_chart ), contentDescription = "trends")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Trends")
            }
            Button(onClick = { navController.navigate("${AppConstants.CATEGORY}/${monthYear.value}")}, modifier = Modifier.constrainAs(categoryBtnId){
                top.linkTo(dividerId.bottom, margin = 10.dp)
                start.linkTo(trendsBtnId.end)
                end.linkTo(parent.end)
            }) {
                Icon(painter = painterResource(id = R.drawable.icon_pie_chart ), contentDescription = "charts" )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Categories")
            }

            Text(text = "Monthly Expenses", fontSize = 18.sp, fontWeight = FontWeight.Bold ,modifier = Modifier.constrainAs(titleId) {
                top.linkTo(addNewBtnId.top)
                bottom.linkTo(addNewBtnId.bottom)
                start.linkTo(parent.start,margin = 16.dp)
                end.linkTo(addNewBtnId.start)
                width = Dimension.fillToConstraints
            })
            Button(onClick = { navController.navigate("${AppConstants.ADD_NEW}/") }, modifier = Modifier.constrainAs(addNewBtnId) {
                top.linkTo(categoryBtnId.bottom, margin = 20.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "addIcon")
                Spacer(modifier = Modifier.width(5.dp))
                Text("Add")
            }


            ItemListView(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(listId) {
                    top.linkTo(titleId.bottom, margin = 10.dp)
                },viewModel,navController)
        }
    }

}

@Composable
fun TopCardView(modifier: Modifier,title:String,amount:String){
    Card(modifier=modifier.padding(start = 16.dp, top = 20.dp),elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = amount)
        }
    }
}



@Composable
fun ItemListView(modifier: Modifier,viewModel: HomeViewModel,navController: NavController){
    val expList by  viewModel.expenseByMonthLiveData.collectAsState(initial = emptyList())
     LazyColumn( modifier = modifier
     ) {
         items(expList.toList())  { item ->
            SingleItemView(item) {
                navController.navigate("${AppConstants.ADD_NEW}/$it")
            }
         }
     }
}

@Composable
fun SingleItemView(model: ExpenseModel,onClick:(id:Int)->Unit){

      Card(onClick = { onClick(model.expId) }, modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(16.dp),
          elevation = CardDefaults.cardElevation(4.dp),
          shape = RoundedCornerShape(8.dp)
      ) {
          ConstraintLayout(modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)) {
              val (iconId,titleId,amountId,dateId) = createRefs()

              Icon(painter = painterResource(id = R.drawable.icon_round_work),
             contentDescription = "category", modifier = Modifier
                      .constrainAs(iconId) {
                          top.linkTo(parent.top)
                          start.linkTo(parent.start)
                      }
                      .size(40.dp) )
              Text(text = model.title, fontSize = 14.sp, textAlign = TextAlign.Left , modifier = Modifier.constrainAs(titleId) {
                  top.linkTo(parent.top)
                  bottom.linkTo(iconId.bottom)
                  end.linkTo(amountId.start, margin = 10.dp)
                  start.linkTo(iconId.end, margin = 10.dp)
                  width = Dimension.fillToConstraints
              })
              Text(text = "₹${model.amount}",fontSize = 16.sp, modifier = Modifier.constrainAs(amountId) {
                  top.linkTo(parent.top)
                  end.linkTo(parent.end)
              })
              Text(text = model.date.formatDateMonth(),fontSize = 12.sp, modifier = Modifier.constrainAs(dateId) {
                  top.linkTo(amountId.bottom)
                  end.linkTo(parent.end)
              })


      }
      }
}



