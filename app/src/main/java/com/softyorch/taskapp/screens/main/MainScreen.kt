package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.*
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
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
        Content(it = it, taskViewModel = taskViewModel, navController = navController)
    }
}

@Composable
private fun Content(it: PaddingValues, taskViewModel: TaskViewModel, navController: NavController) {

    val tasks = taskViewModel.taskList.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 8.dp, end = 8.dp)
    ) {

        RowIndication(text = "My Tasks", paddingStart = 32.dp)

        Spacer(modifier = Modifier.padding(8.dp))

        RowIndication(text = "To be made...", fontSize = 16.sp, paddingStart = 32.dp)
        if (tasks.isEmpty())
            RowIndication("Añade una nueva tarea...", fontSize = 14.sp)
        else
            FillLazyColumn(tasks = tasks, taskViewModel = taskViewModel, navController = navController)

        Spacer(modifier = Modifier.padding(8.dp))

        RowIndication(text = "Completed in the last 7 days", fontSize = 16.sp, paddingStart = 32.dp)
        if (tasks.isEmpty())
            RowIndication("Aún no has terminado ninguna tarea", fontSize = 14.sp)
        else
            FillLazyColumn(tasks = tasks, taskViewModel = taskViewModel, checkOrNot = true, navController = navController)
    }
}

@Composable
private fun FillLazyColumn(
    tasks: List<Task>,
    taskViewModel: TaskViewModel,
    checkOrNot: Boolean = false,
    navController: NavController
) {
    LazyColumn {
        items(tasks) { task ->
            if (checkOrNot == task.checkState)
                TaskSummaryCheck(
                task.checkState,
                onCheckedChange = {
                    task.checkState = it
                    task.finishDate = if(it) Date.from(Instant.now()) else null
                    taskViewModel.updateTask(task)
                    //Esto debe cambiar, no es correcto, aunque funciona
                    navController.navigate(TaskAppScreens.MainScreen.name)
                },
                text = task.title,
                onclick = {
                    navController.navigate(TaskAppScreens.DetailsScreen.name + "/${task.id}")
                }
            )
        }.apply {
            //TODO Averiguar la lógica para obtener las tareas con check true y mostrar un mensaje
        }
    }
}