package com.softyorch.taskapp.navigation

import android.util.Log

enum class TaskAppScreens {
    SplashScreen,
    LoginScreen,
    MainScreen,
    TaskDetailsScreen,
    TaskHistoryScreen,
    SettingsScreen,
    UserDataScreen;

    companion object {
        fun fromRoute(route: String?): TaskAppScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            MainScreen.name -> MainScreen
            TaskDetailsScreen.name -> TaskDetailsScreen
            TaskHistoryScreen.name -> TaskHistoryScreen
            SettingsScreen.name -> SettingsScreen
            UserDataScreen.name -> UserDataScreen
            null -> LoginScreen
            else -> LoginScreen
        }
    }

}