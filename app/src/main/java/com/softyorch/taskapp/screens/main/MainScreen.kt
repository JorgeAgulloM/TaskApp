package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
        Content(it = it)
    }
}

@Composable
fun Content(it: PaddingValues) {

    val taskViewModel = hiltViewModel<TaskViewModel>()
    val tasks = taskViewModel.taskList.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 32.dp, end = 8.dp)
    ) {
        RowIndication(text = "My Tasks", paddingStart = 16.dp)
        Spacer(modifier = Modifier.padding(8.dp))
        RowIndication(text = "To be made...", fontSize = 16.sp, paddingStart = 16.dp)
        if (tasks.isEmpty())
            RowIndication("Añade una nueva tarea...", fontSize = 14.sp)
        else
            LazyColumn {
                items(tasks) { task ->
                    if (!task.checkState) TaskSummary(task.checkState, text = task.title)
                }
            }
        Spacer(modifier = Modifier.padding(8.dp))
        RowIndication(text = "Completed in the last 7 days", fontSize = 16.sp, paddingStart = 16.dp)
        if (tasks.isEmpty())
            RowIndication("Aún no has terminado ninguna tarea", fontSize = 14.sp)
        else
            LazyColumn {
                items(tasks) { task ->
                    if (task.checkState) TaskSummary(task.checkState, text = task.title)
                }
            }
    }
}