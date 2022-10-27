package com.softyorch.taskapp.ui.navigation

sealed class AppScreensRoutes(val route: String) {
    object LoginScreenBeta: AppScreensRoutes(route = AppScreens.LoginScreenBeta.name)
    object MainScreenBeta: AppScreensRoutes(route = AppScreens.MainScreenBeta.name)
}