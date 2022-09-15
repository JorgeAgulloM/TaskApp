package com.softyorch.taskapp.presentation.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.components.fabCustom.FABCustom
import com.softyorch.taskapp.presentation.components.CheckCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.RowInfo
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
private fun Content(
    it: PaddingValues,
    viewModel: MainViewModel,
    navController: NavController
) {

    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
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

        RowInfoMain(text = stringResource(my_tasks), style = MaterialTheme.typography.titleLarge)
        Divider(modifier = Modifier.padding(start = 8.dp, end = 16.dp, bottom = 8.dp))

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            RowInfoMain(text = stringResource(to_be_made))
            FillLazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(min = 20.dp, max = 280.dp),
                tasks = tasks,
                updateTask = viewModel::updateTask,
                text = stringResource(add_new_task),
                initStateCheck = false,
                enabled = !isLoading
            ) {
                navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}")
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            RowInfoMain(text = stringResource(tasks_completed_last_days))
            FillLazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                tasks = tasks,
                updateTask = viewModel::updateTask,
                text = stringResource(not_yet_complet_any_task),
                initStateCheck = true,
                enabled = !isLoading
            ) {
                navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}")
            }
            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
        }
        if (isLoading)
            CircularIndicatorCustom(
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

@ExperimentalMaterial3Api
@Composable
private fun FillLazyColumn(
    modifier: Modifier,
    tasks: List<Task>,
    updateTask: KSuspendFunction1<Task, Unit>,
    text: String,
    initStateCheck: Boolean,
    enabled: Boolean,
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
                        task = task,
                        onCheckedChange = {
                            task.checkState = it
                            task.finishDate = if (it) Date.from(Instant.now()) else null
                            coroutineScope.launch {
                                updateTask(task)
                            }
                        },
                        enabled = enabled
                    ) {
                        onClick(task.id)
                    }
                }
            }

            val showIcon by remember { derivedStateOf { lazyState.firstVisibleItemIndex > 0 } }
            //val showIcon by remember { derivedStateOf { lazyState.layoutInfo.visibleItemsInfo.last().index < lazyState.layoutInfo.totalItemsCount  } }


            if (showIcon) Icon(
                imageVector = Icons.Filled.KeyboardArrowUp, //if (isLastItem) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(go_to_up),
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp)
                    .clickable {
                        coroutineScope.launch { lazyState.animateScrollToItem(0) }
                    },
                tint = MaterialTheme.colorScheme.primary
            )

            /* val totalItemsCheckedOrUnchecked by remember { mutableStateOf( tasks.filter { it.checkState == initStateCheck }.count() ) }
             Log.d("TOTAILITEMSFILTERED", "totalItemsCheckedOrUnchecked -> $totalItemsCheckedOrUnchecked")

             val itemsFilter by remember { derivedStateOf { lazyState.layoutInfo.totalItemsCount - lazyState.layoutInfo.visibleItemsInfo.count() } }
             val totalVisualItemsFilter by remember {
                 derivedStateOf {
                     if (itemsFilter > 1) {
                         lazyState.layoutInfo.visibleItemsInfo.count()

                     } else if (itemsFilter > 0) {
                         lazyState.layoutInfo.visibleItemsInfo.count() - 1

                     } else {
                         lazyState.layoutInfo.visibleItemsInfo.count() - 2
                     }
                 }
             }

             val showArrow by remember {
                 derivedStateOf {
                     totalVisualItemsFilter + 1 < tasks.filter {
                         it.checkState == initStateCheck
                     }.size
                 }
             }

             if (showArrow) {
                 val isLastItem by remember {
                     derivedStateOf {
                         lazyState.firstVisibleItemIndex ==
                                 lazyState.layoutInfo.totalItemsCount -
                                 totalVisualItemsFilter - 1
                     }
                 }

                 Icon(
                     imageVector = if (isLastItem) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                     contentDescription = stringResource(go_to_up),
                     modifier = Modifier.padding(top = 4.dp, start = 8.dp).clickable {
                         if (isLastItem) {
                             coroutineScope.launch { lazyState.animateScrollToItem(0) }
                         }
                     },
                     tint = MaterialTheme.colorScheme.primary
                 )
             }*/
        } else RowInfo(text = text, paddingStart = 16.dp)
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
private fun CheckCustomMain(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(value = false) }
    val density = LocalDensity.current
    rememberCoroutineScope().launch {
        delay(100)
        visible = true
    }
/*    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally {
            with(density) { -100.dp.roundToPx() }
        } + expandHorizontally(
            expandFrom = Alignment.Start
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut()
    )
    {*/
        CheckCustom(
            checked = task.checkState,
            onCheckedChange = {
                visible = false
                onCheckedChange(it)
            },
            enabled = enabled,
            text = task.title,
            onClick = { onClick() }
        )
    //}
}
