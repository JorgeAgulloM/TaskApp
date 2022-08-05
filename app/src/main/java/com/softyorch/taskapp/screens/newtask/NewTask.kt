package com.softyorch.taskapp.screens.newtask

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(navController: NavController) {
    Hello("New Task")
    Scaffold(topBar = {
        TopAppBar(
            title = "New Task",
            icon = Icons.Rounded.Settings,
            nameScreen = TaskAppScreens.SettingsScreen.name,
            navController = navController,
        )
    }) {}
}