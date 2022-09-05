package com.softyorch.taskapp.presentation.screens.main

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.reflect.KSuspendFunction1

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

    Column(
        modifier = Modifier.fillMaxSize(1f)
            .padding(
                top = it.calculateTopPadding() + 8.dp,
                bottom = 8.dp,
                start = 8.dp,
                end = 8.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {

        RowInfoMain(text = "My Tasks")
        Divider(modifier = Modifier.padding(start = 8.dp, end = 16.dp, bottom = 8.dp))

        Column(
            //modifier = Modifier.heightIn(min = 20.dp, max = 290.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            RowInfoMain(text = "To be made...")
            FillLazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(min = 20.dp, max = 280.dp),
                tasks = tasks,
                updateTask = viewModel::updateTask,
                text = "Añade una nueva tarea...",
                initStateCheck = false
            ) {
                navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}")
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
        }

        Column(
            //modifier = Modifier.heightIn(min = 20.dp, max = 290.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            RowInfoMain(text = "Completed in the last 7 days")
            FillLazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f),
                tasks = tasks,
                updateTask = viewModel::updateTask,
                text = "Aún no has terminado ninguna tarea",
                initStateCheck = true,
            ) {
                navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}")
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
        }
    }
}

@Composable
private fun RowInfoMain(text: String) {
    RowInfo(text = text, paddingStart = 32.dp)
}

@ExperimentalMaterial3Api
@Composable
private fun FillLazyColumn(
    modifier: Modifier,
    tasks: List<Task>,
    updateTask: KSuspendFunction1<Task, Unit>,
    text: String,
    initStateCheck: Boolean,
    onClick: (UUID) -> Unit
) {

    val lazyState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    if (tasks.any { it.checkState == initStateCheck })
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LazyColumn(
                modifier = modifier,
                state = lazyState,
                userScrollEnabled = true,
                flingBehavior = ScrollableDefaults.flingBehavior()
            ) {
                items(tasks.filter { it.checkState == initStateCheck }) { task ->
                    CheckCustomMain(
                        task = task, onCheckedChange = {
                            task.checkState = it
                            task.finishDate = if (it) Date.from(Instant.now()) else null
                            coroutineScope.launch {
                                updateTask(task)
                            }
                        }
                    ) {
                        onClick(task.id)
                    }
                }
            }

            val showArrow by remember {
                derivedStateOf {
                    lazyState.layoutInfo.visibleItemsInfo.count() < tasks.filter {
                        it.checkState == initStateCheck
                    }.size
                }
            }

            if (showArrow) {
                val isLastItem by remember {
                    derivedStateOf {
                        lazyState.firstVisibleItemIndex ==
                                lazyState.layoutInfo.totalItemsCount -
                                lazyState.layoutInfo.visibleItemsInfo.count()
                    }
                }

                Icon(
                    imageVector = if (isLastItem) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Go to Up",
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp).clickable {
                        if (isLastItem) {
                            coroutineScope.launch { lazyState.animateScrollToItem(0) }
                        }
                    }
                )
            }
        } else RowInfo(text = text, paddingStart = 16.dp)
}

@ExperimentalMaterial3Api
@Composable
private fun CheckCustomMain(
    task: Task, onCheckedChange: (Boolean) -> Unit, onClick: () -> Unit
) {
    CheckCustom(
        checked = task.checkState,
        onCheckedChange = { onCheckedChange(it) },
        text = task.title,
        onClick = { onClick() }
    )
}