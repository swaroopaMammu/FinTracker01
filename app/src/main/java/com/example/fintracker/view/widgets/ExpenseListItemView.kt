package com.example.fintracker.view.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.fintracker.R
import com.example.fintracker.data.db.entity.ExpenseModel
import com.example.fintracker.utils.formatDateMonth

@Composable
fun SingleItemView(model: ExpenseModel, onClick:(id:Int)->Unit){

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