package com.softyorch.taskapp.navigation

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
import com.softyorch.taskapp.ui.screens.userdata.UserDataViewModel
import com.softyorch.taskapp.ui.screens.userdata.UserDataScreen

@ExperimentalMaterial3Api
@Composable
fun TaskAppNavigation(reloadComposable: () -> Unit) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val userDataViewModel = hiltViewModel<UserDataViewModel>()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name) {
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
        composable(route = AppScreensRoutes.MainScreen.route) {
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
            SettingsScreen(navController = navController, reloadComposable = reloadComposable)
        }
        composable(route = "${AppScreensRoutes.UserDataScreen.route}/{id}", arguments = listOf(
            navArgument(name = "id") {
                type = NavType.StringType
            }
        )
        ) { navBack ->
            navBack.arguments?.getString("id").let { id ->
                UserDataScreen(
                    navController = navController,
                    userDataViewModel = userDataViewModel,
                    id = id.toString()
                )
            }
        }
    }
}
