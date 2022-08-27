package com.softyorch.taskapp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.StandardizedSizes
import java.time.Instant
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {
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
            FABCustom()
        },
    ) {
        Content(it = it, viewModel = mainViewModel, navController = navController)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Content(it: PaddingValues, viewModel: MainViewModel, navController: NavController) {

    val tasks = viewModel.taskList.collectAsState().value
    val textSizes = viewModel.sizeSelectedOfUser()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 8.dp, end = 8.dp)
    ) {

        RowInfoMain(text = "My Tasks", textSizes = textSizes)
        Spacer(modifier = Modifier.padding(8.dp))
        RowInfoMain(text = "To be made...", textSizes = textSizes)
        if (tasks.isEmpty())
            RowInfo(
                text = "Añade una nueva tarea...", textSizes = textSizes
            )
        else
            FillLazyColumn(
                tasks = tasks, mainViewModel = viewModel, navController = navController
            )
        Spacer(modifier = Modifier.padding(8.dp))
        RowInfoMain(text = "Completed in the last 7 days", textSizes = textSizes)
        if (tasks.isEmpty())
            RowInfo(
                text = "Aún no has terminado ninguna tarea", textSizes = textSizes
            )
        else
            FillLazyColumn(
                tasks = tasks, mainViewModel = viewModel, checkOrNot = true,
                navController = navController
            )
    }
}

@Composable
private fun RowInfoMain(text: String, textSizes: StandardizedSizes) {
    RowInfo(text = text, paddingStart = 32.dp, textSizes = textSizes)
}

@ExperimentalMaterial3Api
@Composable
private fun FillLazyColumn(
    tasks: List<Task>,
    mainViewModel: MainViewModel,
    checkOrNot: Boolean = false,
    navController: NavController
) {
    LazyColumn {
        items(tasks) { task ->
            if (checkOrNot == task.checkState)
                CheckCustomMain(task, mainViewModel, navController)
            else Text("Añade alguna tarea")
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CheckCustomMain(
    task: Task,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    CheckCustom(
        checked = task.checkState,
        onCheckedChange = {
            task.checkState = it
            task.finishDate = if (it) Date.from(Instant.now()) else null
            mainViewModel.updateTask(task)
            //Esto debe cambiar, no es correcto, aunque funciona
            navController.popBackStack()
            navController.navigate(AppScreensRoutes.MainScreen.route)
        },
        text = task.title,
        onClick = {
            navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
        }
    )
}