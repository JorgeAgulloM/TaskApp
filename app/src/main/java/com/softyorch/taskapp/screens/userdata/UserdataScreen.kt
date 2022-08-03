package com.softyorch.taskapp.screens.userdata

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SupervisedUserCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.TopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserdataScreen(navController: NavHostController) {
    Hello("Userdata")
    Scaffold(topBar = {
        TopAppBar(
            title = "User Data",
            icon = Icons.Rounded.SupervisedUserCircle,
            nameScreen = TaskAppScreens.UserDataScreen.name,
            navController = navController,
        )
    }) {}
}