package com.softyorch.taskapp.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softyorch.taskapp.ui.screens.detail.DetailScreen
import com.softyorch.taskapp.ui.screens.history.HistoryScreen
import com.softyorch.taskapp.ui.screens.login.LoginScreen
import com.softyorch.taskapp.ui.screens.main.MainScreen
import com.softyorch.taskapp.ui.screens.main.MainViewModel
import com.softyorch.taskapp.ui.screens.settings.SettingsScreen
import com.softyorch.taskapp.ui.screens.splash.SplashScreen
import com.softyorch.taskapp.ui.screens.splash.SplashViewModel
import com.softyorch.taskapp.ui.screens.userdata.UserDataScreen
import com.softyorch.taskapp.ui.screensBeta.login.LoginScreenBeta
import com.softyorch.taskapp.ui.screensBeta.main.MainScreenBeta

@ExperimentalMaterial3Api
@Composable
fun TaskAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreensRoutes.LoginScreenBeta.route) {
        composable(route = AppScreensRoutes.SplashScreen.route) {
            val splashViewModel = hiltViewModel<SplashViewModel>()
            SplashScreen(
                navController = navController,
                viewModel = splashViewModel
            )
        }
        composable(route = AppScreensRoutes.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreensRoutes.LoginScreenBeta.route){
            LoginScreenBeta(navController = navController)
        }
        composable(route = AppScreensRoutes.MainScreenBeta.route){
            MainScreenBeta(navController)
        }
        composable(route = AppScreensRoutes.MainScreen.route) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(route = "${AppScreensRoutes.DetailScreen.route}/{id}", arguments = listOf(
            navArgument(name = "id") {
                type = NavType.StringType
            }
        )
        ) { navBack ->
            navBack.arguments?.getString("id").let { id ->
                DetailScreen(
                    navController = navController,
                    id = id.toString()
                )
            }
        }
        composable(route = AppScreensRoutes.HistoryScreen.route) {
            HistoryScreen(navController = navController)
        }
        composable(route = AppScreensRoutes.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = AppScreensRoutes.UserDataScreen.route) {
            UserDataScreen(navController = navController)//, getUserImage = getUserImage)
        }
    }
}
