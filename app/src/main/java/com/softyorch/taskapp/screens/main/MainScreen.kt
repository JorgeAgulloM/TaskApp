package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Main",
                icon = Icons.Rounded.Home,
                isMainScreen = true,
                nameScreen = TaskAppScreens.MainScreen.name,
                navController = navController,
            )
        },
        floatingActionButton = {
            FAB(
                navController = navController
            )
        },
    ) {
        Content(it)
    }
}

@Composable
fun Content(it: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 32.dp, end = 8.dp)
    ) {
        RowIndication(text = "My Tasks", paddingStart = 16.dp)
        RowIndication(text = "To be made...", fontSize = 16.sp, paddingStart = 16.dp)
        //Aquí debe ir un LazyColumn
        Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {

            TaskSummary(text = "Tarea número 1 de prueba")
            TaskSummary(text = "Tarea número 2 de prueba")
            TaskSummary(text = "Tarea número 3 de prueba")
            TaskSummary(text = "Tarea número 4 de prueba")

        }
        RowIndication(text = "Completed in the last 7 days", fontSize = 16.sp, paddingStart = 16.dp)
        //Aquí debe ir un LazyColumn
        Column(modifier = Modifier.fillMaxWidth().height(300.dp)) {

            TaskSummary(checked = true, text = "Tarea número 10 de prueba")
            TaskSummary(checked = true, text = "Tarea número 11 de prueba")
            TaskSummary(checked = true, text = "Tarea número 12 de prueba")


        }
    }
}