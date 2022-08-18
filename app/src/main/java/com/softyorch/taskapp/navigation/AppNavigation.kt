package com.softyorch.taskapp.navigation

import android.content.SharedPreferences
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softyorch.taskapp.model.Settings
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.screens.detail.DetailScreen
import com.softyorch.taskapp.screens.history.HistoryScreen
import com.softyorch.taskapp.screens.login.LoginScreen
import com.softyorch.taskapp.screens.main.MainScreen
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.screens.settings.SettingsViewModel
import com.softyorch.taskapp.screens.settings.SettingsScreen
import com.softyorch.taskapp.screens.splash.SplashScreen
import com.softyorch.taskapp.screens.splash.SplashViewModel
import com.softyorch.taskapp.screens.userdata.UserDataViewModel
import com.softyorch.taskapp.screens.userdata.UserDataScreen

@ExperimentalMaterial3Api
@Composable
fun TaskAppNavigation(sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    val taskViewModel = hiltViewModel<TaskViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    PrepareSettingsFirstTime(settingsViewModel)
    val userDataViewModel = hiltViewModel<UserDataViewModel>()
    //PrepareUserDataFirstTime(userDataViewModel)

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name) {
        composable(route = AppScreensRoutes.SplashScreen.route) {
            val splashViewModel = hiltViewModel<SplashViewModel>()
            SplashScreen(
                navController = navController,
                splashViewModel = splashViewModel,
                sharedPreferences = sharedPreferences
            )
        }
        composable(route = AppScreensRoutes.LoginScreen.route) {
            LoginScreen(navController = navController, sharedPreferences = sharedPreferences)
        }
        composable(route = AppScreensRoutes.MainScreen.route) {
            MainScreen(navController = navController, taskViewModel = taskViewModel)
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
                    taskViewModel = taskViewModel,
                    id = id.toString()
                )
            }
        }
        composable(route = AppScreensRoutes.HistoryScreen.route) {
            HistoryScreen(navController = navController, taskViewModel = taskViewModel)
        }
        composable(route = AppScreensRoutes.SettingsScreen.route) {
            SettingsScreen(navController = navController, settingsViewModel = settingsViewModel)
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

/*@Composable
private fun PrepareUserDataFirstTime(userDataViewModel: UserDataViewModel) {
    if (userDataViewModel.userDataList.collectAsState().value.isEmpty()) {
        userDataViewModel.addUserData(
            userData = UserData(
                username = "Unknown",
                userEmail = "Unknown",
                userPass = "Unknown",
                userPicture = null,
                rememberMe = false
            )
        )
    }
}*/
