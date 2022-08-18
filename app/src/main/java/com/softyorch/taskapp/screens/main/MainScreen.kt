package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.components.TopAppBarCustom
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.*
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Main",
                isMainScreen = true,
                nameScreen = AppScreens.MainScreen.name,
                navController = navController,
            )
        },
        floatingActionButton = {
            FAB(
                navController = navController,
                taskViewModel = taskViewModel
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
            FillLazyColumn(
                tasks = tasks,
                taskViewModel = taskViewModel,
                navController = navController
            )

        Spacer(modifier = Modifier.padding(8.dp))

        RowIndication(text = "Completed in the last 7 days", fontSize = 16.sp, paddingStart = 32.dp)
        if (tasks.isEmpty())
            RowIndication("Aún no has terminado ninguna tarea", fontSize = 14.sp)
        else
            FillLazyColumn(
                tasks = tasks,
                taskViewModel = taskViewModel,
                checkOrNot = true,
                navController = navController
            )
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
                    checked = task.checkState,
                    onCheckedChange = {
                        task.checkState = it
                        task.finishDate = if (it) Date.from(Instant.now()) else null
                        taskViewModel.updateTask(task)
                        //Esto debe cambiar, no es correcto, aunque funciona
                        navController.popBackStack()
                        navController.navigate(AppScreensRoutes.MainScreen.route)
                    },
                    text = task.title,
                    onClick = {
                        navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
                    }
                )
        }.apply {
            //TODO Averiguar la lógica para obtener las tareas con check true y mostrar un mensaje
        }
    }
}