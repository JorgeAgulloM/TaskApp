package com.softyorch.taskapp.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.ELEVATION_DP
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.reflect.KSuspendFunction1

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(main),
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
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val taskEntities: List<TaskEntity> by viewModel.taskEntityList.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(
                top = it.calculateTopPadding() + 8.dp,
                bottom = 8.dp,
                start = 8.dp,
                end = 8.dp
            ),
        verticalArrangement = Arrangement.Top
    ) {

        RowInfoMain(text = stringResource(my_tasks), style = MaterialTheme.typography.titleLarge)
        Divider(modifier = Modifier.padding(start = 8.dp, end = 16.dp, bottom = 8.dp))

        FillLazyColumnNoCheckeds(
            taskEntities = taskEntities.filter { !it.checkState },
            updateTaskEntity = viewModel::updateTask,
            enabled = !isLoading,
        ) { navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}") }

        Spacer(modifier = Modifier.padding(8.dp))

        FillLazyColumnCheckeds(
            taskEntities = taskEntities.filter { it.checkState },
            updateTaskEntity = viewModel::updateTask,
            enabled = !isLoading,
        ) { navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}") }

        if (isLoading) CircularIndicatorCustom(
            text = stringResource(loading_loading),
            modifier = Modifier
                .safeContentPadding()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun RowInfoMain(text: String, style: TextStyle = MaterialTheme.typography.titleMedium) {
    RowInfo(text = text, paddingStart = 32.dp, style = style)
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
private fun FillLazyColumnNoCheckeds(
    taskEntities: List<TaskEntity>,
    updateTaskEntity: KSuspendFunction1<TaskEntity, Unit>,
    enabled: Boolean,
    onClick: (UUID) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    Column(
        modifier = Modifier
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        RowInfoMain(text = stringResource(to_be_made))
        if (taskEntities.any { !it.checkState })
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 20.dp, max = 260.dp),
                    state = lazyState,
                    userScrollEnabled = true,
                    flingBehavior = ScrollableDefaults.flingBehavior()
                ) {

                    items(taskEntities) { task ->
                        var myCheck by remember { mutableStateOf(value = task.checkState) }

                        CheckCustomMain(
                            checked = myCheck,
                            title = task.title,
                            onCheckedChange = {
                                myCheck = it
                                task.checkState = it
                                task.finishDate = if (it) Date.from(Instant.now()) else null
                                coroutineScope.launch {
                                    delay(400)
                                    myCheck = !it
                                    updateTaskEntity(task)
                                }
                            },
                            enabled = enabled
                        ) {
                            onClick(task.id)
                        }
                    }
                }

                val showIcon by remember { derivedStateOf { lazyState.firstVisibleItemIndex > 0 } }
                val itemsFilter by remember { derivedStateOf { lazyState.layoutInfo.totalItemsCount - lazyState.layoutInfo.visibleItemsInfo.count() } }

                if (itemsFilter > 0) {
                    Icon(
                        imageVector = if (showIcon) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(go_to_up),
                        modifier = Modifier
                            .padding(top = 4.dp, start = 8.dp)
                            .clickable {
                                if (showIcon) coroutineScope.launch { lazyState.animateScrollToItem(0) }
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else Box(modifier = Modifier.height(28.dp)) {}
            } else RowInfo(text = stringResource(add_new_task), paddingStart = 16.dp)
        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
    }
}

@ExperimentalMaterial3Api
@Composable
private fun FillLazyColumnCheckeds(
    taskEntities: List<TaskEntity>,
    updateTaskEntity: KSuspendFunction1<TaskEntity, Unit>,
    enabled: Boolean,
    onClick: (UUID) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    Column(
        modifier = Modifier
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        RowInfoMain(text = stringResource(tasks_completed_last_days))
        if (taskEntities.any { it.checkState })
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.83f),
                    state = lazyState,
                    userScrollEnabled = true,
                    flingBehavior = ScrollableDefaults.flingBehavior()
                ) {
                    items(taskEntities) { task ->
                        var myCheck by remember { mutableStateOf(value = task.checkState) }

                        CheckCustomMain(
                            checked = myCheck,
                            title = task.title,
                            onCheckedChange = {
                                myCheck = it
                                task.checkState = it
                                task.finishDate = if (it) Date.from(Instant.now()) else null
                                coroutineScope.launch {
                                    delay(400)
                                    myCheck = !it
                                    updateTaskEntity(task)
                                }
                            },
                            enabled = enabled
                        ) {
                            onClick(task.id)
                        }
                    }
                }

                val showIcon by remember { derivedStateOf { lazyState.firstVisibleItemIndex > 0 } }
                val itemsFilter by remember { derivedStateOf { lazyState.layoutInfo.totalItemsCount - lazyState.layoutInfo.visibleItemsInfo.count() } }

                if (itemsFilter > 0) {
                    Icon(
                        imageVector = if (showIcon) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(go_to_up),
                        modifier = Modifier
                            .padding(top = 4.dp, start = 8.dp)
                            .clickable {
                                if (showIcon) coroutineScope.launch { lazyState.animateScrollToItem(0) }
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else Box(modifier = Modifier.height(28.dp)) {}
            } else RowInfo(text = stringResource(not_yet_complet_any_task), paddingStart = 16.dp)
        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
private fun CheckCustomMain(
    checked: Boolean,
    title: String,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    CheckCustom(
        checked = checked,
        onCheckedChange = {
            onCheckedChange(it)
        },
        enabled = enabled,
        animated = true,
        text = title,
        onClick = { onClick() }
    )
}
