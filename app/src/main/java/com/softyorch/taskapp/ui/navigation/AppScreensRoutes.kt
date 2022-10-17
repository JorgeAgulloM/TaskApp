package com.softyorch.taskapp.ui.navigation

sealed class AppScreensRoutes(val route: String) {
    object SplashScreen: AppScreensRoutes(route = AppScreens.SplashScreen.name)
    object LoginScreen: AppScreensRoutes(route = AppScreens.LoginScreen.name)
    object LoginScreenBeta: AppScreensRoutes(route = AppScreens.LoginScreenBeta.name)
    object MainScreen: AppScreensRoutes(route = AppScreens.MainScreen.name)
    object MainScreenBeta: AppScreensRoutes(route = AppScreens.MainScreenBeta.name)
    object DetailScreen: AppScreensRoutes(route = AppScreens.DetailsScreen.name)
    object HistoryScreen: AppScreensRoutes(route = AppScreens.HistoryScreen.name)
    object SettingsScreen: AppScreensRoutes(route = AppScreens.SettingsScreen.name)
    object UserDataScreen: AppScreensRoutes(route = AppScreens.UserDataScreen.name)
}