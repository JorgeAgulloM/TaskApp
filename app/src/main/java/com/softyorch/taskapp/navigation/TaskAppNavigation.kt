package com.softyorch.taskapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softyorch.taskapp.screens.detail.DetailScreen
import com.softyorch.taskapp.screens.history.HistoryScreen
import com.softyorch.taskapp.screens.login.LoginScreen
import com.softyorch.taskapp.screens.main.MainScreen
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.screens.newtask.NewTask
import com.softyorch.taskapp.screens.settings.SettingsScreen
import com.softyorch.taskapp.screens.splash.SplashScreen
import com.softyorch.taskapp.screens.userdata.UserdataScreen

@Composable
fun TaskAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = TaskAppScreens.SplashScreen.name){
        composable(TaskAppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(TaskAppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(TaskAppScreens.MainScreen.name) {
            MainScreen(navController = navController)
        }
        composable(TaskAppScreens.NewTaskScreen.name){
            NewTask(navController = navController)
        }
        composable(TaskAppScreens.DetailsScreen.name) {
            DetailScreen(navController = navController)
        }
        composable(TaskAppScreens.HistoryScreen.name) {
            HistoryScreen(navController = navController)
        }
        composable(TaskAppScreens.SettingsScreen.name) {
            SettingsScreen(navController = navController)
        }
        composable(TaskAppScreens.UserDataScreen.name) {
            UserdataScreen(navController = navController)
        }
    }
}