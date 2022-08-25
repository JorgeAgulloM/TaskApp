package com.softyorch.taskapp.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.components.fabCustom.FABCustom
import com.softyorch.taskapp.components.CheckCustom
import com.softyorch.taskapp.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.widgets.RowInfo
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

        RowInfo(text = "My Tasks", paddingStart = 32.dp, textSizes = textSizes)

        Spacer(modifier = Modifier.padding(8.dp))

        RowInfo(
            text = "To be made...",
            paddingStart = 32.dp,
            textSizes = textSizes
        )
        if (tasks.isEmpty())
            RowInfo(
                "Añade una nueva tarea...",
                textSizes = textSizes
            )
        else
            FillLazyColumn(
                tasks = tasks,
                mainViewModel = viewModel,
                navController = navController
            )

        Spacer(modifier = Modifier.padding(8.dp))

        RowInfo(
            text = "Completed in the last 7 days",
            paddingStart = 32.dp,
            textSizes = textSizes
        )
        if (tasks.isEmpty())
            RowInfo(
                "Aún no has terminado ninguna tarea",
                textSizes = textSizes
            )
        else
            FillLazyColumn(
                tasks = tasks,
                mainViewModel = viewModel,
                checkOrNot = true,
                navController = navController
            )
    }
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
        }.apply {
            //TODO Averiguar la lógica para obtener las tareas con check true y mostrar un mensaje
        }
    }
}