package com.softyorch.taskapp.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.presentation.components.fabCustom.FABCustom
import com.softyorch.taskapp.presentation.components.CheckCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.RowInfo
import com.softyorch.taskapp.utils.StandardizedSizes
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val isLoading: Boolean by mainViewModel.isLoading.observeAsState(initial = false)
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
        if (isLoading) CircularIndicatorCustom(text = "..loading")
        Content(it = it, viewModel = mainViewModel, navController = navController)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Content(it: PaddingValues, viewModel: MainViewModel, navController: NavController) {

    val tasks: List<Task> by viewModel.taskList.observeAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val textSizes = viewModel.sizeSelectedOfUser()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 8.dp, end = 8.dp)
    ) {

        RowInfoMain(text = "My Tasks", textSizes = textSizes)
        Spacer(modifier = Modifier.padding(8.dp))
        RowInfoMain(text = "To be made...", textSizes = textSizes)
        FillLazyColumn(
            tasks = tasks, mainViewModel = viewModel, navController = navController,
            text = "Añade una nueva tarea...", textSizes = textSizes, checked = false
        )
        Spacer(modifier = Modifier.padding(8.dp))
        RowInfoMain(text = "Completed in the last 7 days", textSizes = textSizes)
        FillLazyColumn(
            tasks = tasks, mainViewModel = viewModel, navController = navController,
            textSizes = textSizes, text = "Aún no has terminado ninguna tarea", checked = true
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
    navController: NavController,
    textSizes: StandardizedSizes,
    text: String,
    checked: Boolean
) {
    if (tasks.any { it.checkState == checked }) LazyColumn {
        items(tasks.filter { it.checkState == checked }) { task ->
            CheckCustomMain(
                task = task, mainViewModel = mainViewModel, navController = navController,
                textSizes = textSizes.normalSize
            ) {
                navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
            }
        }
    } else RowInfo(text = text, paddingStart = 16.dp, textSizes = textSizes)
}

@ExperimentalMaterial3Api
@Composable
private fun CheckCustomMain(
    task: Task,
    mainViewModel: MainViewModel,
    navController: NavController,
    textSizes: TextUnit,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    CheckCustom(
        checked = task.checkState,
        onCheckedChange = {
            task.checkState = it
            task.finishDate = if (it) Date.from(Instant.now()) else null
            coroutineScope.launch { mainViewModel.updateTask(task) }
            //Esto debe cambiar, no es correcto, aunque funciona
            /* navController.popBackStack()
             navController.navigate(AppScreensRoutes.MainScreen.route)*/
        },
        text = task.title,
        onClick = {
            onClick()
        },
        textSizes = textSizes
    )
}