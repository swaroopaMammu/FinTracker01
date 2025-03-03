package com.example.fintracker.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.fintracker.data.db.AppDatabase
import com.example.fintracker.data.repository.ExpenseRepository
import com.example.fintracker.ui.theme.FinTrackerTheme
import com.example.fintracker.view.navigation.AppNavigationComponent
import com.example.fintracker.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigationComponent()
                }
            }
        }

    }
}