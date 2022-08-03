package com.softyorch.taskapp.screens.main

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Hello("Main")
    Scaffold(topBar = {
        TopAppBar(
            title = "Main",
            icon = Icons.Rounded.Home,
            isMainScreen = true,
            nameScreen = TaskAppScreens.MainScreen.name,
            navController = navController,
        )
    }) {}
}