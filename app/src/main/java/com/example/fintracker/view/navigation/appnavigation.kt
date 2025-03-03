package com.example.fintracker.view.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fintracker.utils.AppConstants
import com.example.fintracker.utils.getCurrentYearMonth
import com.example.fintracker.view.screens.AddNewExpense
import com.example.fintracker.view.screens.CategoryScreen
import com.example.fintracker.view.screens.HomeScreen
import com.example.fintracker.view.screens.TrendsScreen
import com.example.fintracker.viewmodel.HomeViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationComponent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppConstants.HOME
    ) {
        composable(AppConstants.HOME) { HomeScreen(navController) }
        composable("${AppConstants.ADD_NEW}/{id}") { backStackEntry ->
            val data = backStackEntry.arguments?.getString("id") ?:""
            AddNewExpense(navController,data) }
        composable("${AppConstants.CATEGORY}/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: getCurrentYearMonth()
            CategoryScreen(navController,date) }
        composable("${AppConstants.TRENDS}/{date}") {backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: getCurrentYearMonth()
            TrendsScreen(navController,date) }
    }
}