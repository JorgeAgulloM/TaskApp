package com.softyorch.taskapp.ui.navigation

sealed class AppScreensRoutes(val route: String) {
    object LoginScreenBeta: AppScreensRoutes(route = AppScreens.LoginScreenBeta.name)
    object MainScreenBeta: AppScreensRoutes(route = AppScreens.MainScreenBeta.name)
    object SettingsScreen: AppScreensRoutes(route = AppScreens.SettingsScreen.name)
    object UserDataScreen: AppScreensRoutes(route = AppScreens.UserDataScreen.name)
}