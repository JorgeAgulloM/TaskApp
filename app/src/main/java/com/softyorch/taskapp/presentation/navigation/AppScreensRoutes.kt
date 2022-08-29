package com.softyorch.taskapp.presentation.navigation

sealed class AppScreensRoutes(val route: String) {
    object SplashScreen: AppScreensRoutes(route = AppScreens.SplashScreen.name)
    object LoginScreen: AppScreensRoutes(route = AppScreens.LoginScreen.name)
    object MainScreen: AppScreensRoutes(route = AppScreens.MainScreen.name)
    object DetailScreen: AppScreensRoutes(route = AppScreens.DetailsScreen.name)
    object HistoryScreen: AppScreensRoutes(route = AppScreens.HistoryScreen.name)
    object SettingsScreen: AppScreensRoutes(route = AppScreens.SettingsScreen.name)
    object UserDataScreen: AppScreensRoutes(route = AppScreens.UserDataScreen.name)
}