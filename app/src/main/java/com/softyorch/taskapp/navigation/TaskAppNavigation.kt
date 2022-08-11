package com.softyorch.taskapp.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softyorch.taskapp.model.Settings
import com.softyorch.taskapp.screens.detail.DetailScreen
import com.softyorch.taskapp.screens.history.HistoryScreen
import com.softyorch.taskapp.screens.login.LoginScreen
import com.softyorch.taskapp.screens.main.MainScreen
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.screens.settings.SettingsViewModel
import com.softyorch.taskapp.screens.settings.SettingsScreen
import com.softyorch.taskapp.screens.splash.SplashScreen
import com.softyorch.taskapp.screens.userdata.UserDataViewModel
import com.softyorch.taskapp.screens.userdata.UserdataScreen

@Composable
fun TaskAppNavigation() {
    val navController = rememberNavController()
    val taskViewModel = hiltViewModel<TaskViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val userDataViewModel = hiltViewModel<UserDataViewModel>()

    PrepareSettingsFirstTime(settingsViewModel)

    NavHost(navController = navController, startDestination = TaskAppScreens.SplashScreen.name) {
        composable(TaskAppScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(TaskAppScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(TaskAppScreens.MainScreen.name) {
            MainScreen(navController = navController, taskViewModel = taskViewModel)
        }
        val route = TaskAppScreens.DetailsScreen.name
        composable("$route/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                }
            )
        ) { navBack ->
            navBack.arguments?.getString("id").let { id ->
                val viewModel = hiltViewModel<TaskViewModel>()
                DetailScreen(navController = navController, taskViewModel = viewModel, id = id.toString())
            }
        }
        composable(TaskAppScreens.HistoryScreen.name) {
            HistoryScreen(navController = navController, taskViewModel = taskViewModel)
        }
        composable(TaskAppScreens.SettingsScreen.name) {
            SettingsScreen(navController = navController, settingsViewModel = settingsViewModel)
        }
        composable(TaskAppScreens.UserDataScreen.name) {
            UserdataScreen(navController = navController, userDataViewModel = userDataViewModel)
        }
    }
}

@Composable
private fun PrepareSettingsFirstTime(settingsViewModel: SettingsViewModel) {
    if (settingsViewModel.settingsList.collectAsState().value.isEmpty()) {
        settingsViewModel.insertPreferences(
            settings = Settings(
                id = 0,
                lightDarkAutomaticTheme = true,
                lightOrDarkTheme = false,
                automaticLanguage = true,
                automaticColors = false,
                preferenceBooleanFive = false
            )
        )
    }
}