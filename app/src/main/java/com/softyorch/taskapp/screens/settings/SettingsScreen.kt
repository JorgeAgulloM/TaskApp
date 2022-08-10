package com.softyorch.taskapp.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TaskSwitch
import com.softyorch.taskapp.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Hello("Settings")
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Settings",
                icon = Icons.Rounded.Settings,
                nameScreen = TaskAppScreens.SettingsScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, navController = navController)
        })
}

@Composable
private fun Content(it: PaddingValues, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f)) {
        TaskSwitch(
            "Light/Dark Automatic Theme"
        )
        TaskSwitch(
            "Manual light/dark theme",
            checked = true
        )
        TaskSwitch(
            "Automatic language",
            checked = true
        )
        TaskSwitch(
            "Automatic colors"
        )
    }
}