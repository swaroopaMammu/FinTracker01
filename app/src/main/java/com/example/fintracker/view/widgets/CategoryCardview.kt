package com.example.fintracker.view.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fintracker.utils.CategoryLabels

@Composable
fun CategoryDropdown(category:String="Category",onClick: (category: CategoryLabels)->Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = CategoryLabels.entries.toTypedArray()
    var selectedItem by remember { mutableStateOf(category) }


    Card(  modifier = Modifier
        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = selectedItem, color = Color.White)
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        selectedItem = item.name
                        onClick.invoke(CategoryLabels.valueOf(selectedItem))
                        expanded = false
                    }
                )
            }
        }
    }

}