package com.softyorch.taskapp.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softyorch.taskapp.ui.screensBeta.login.LoginScreenBeta
import com.softyorch.taskapp.ui.screensBeta.main.MainScreenBeta

@ExperimentalMaterial3Api
@Composable
fun TaskAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreensRoutes.LoginScreenBeta.route) {
        composable(route = AppScreensRoutes.LoginScreenBeta.route){
            LoginScreenBeta(navController = navController)
        }
        composable(route = AppScreensRoutes.MainScreenBeta.route){
            MainScreenBeta(navController)
        }
    }
}
