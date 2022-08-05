package com.softyorch.taskapp.navigation

enum class TaskAppScreens {
    SplashScreen,
    LoginScreen,
    MainScreen,
    NewTaskScreen,
    DetailsScreen,
    HistoryScreen,
    SettingsScreen,
    UserDataScreen;

    companion object {
        fun fromRoute(route: String?): TaskAppScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            MainScreen.name -> MainScreen
            NewTaskScreen.name -> NewTaskScreen
            DetailsScreen.name -> DetailsScreen
            HistoryScreen.name -> HistoryScreen
            SettingsScreen.name -> SettingsScreen
            UserDataScreen.name -> UserDataScreen
            null -> LoginScreen
            else -> LoginScreen
        }
    }

}