package com.softyorch.taskapp.ui.screens.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Configuration.*
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.components.CircularIndicatorCustomDialog
import com.softyorch.taskapp.ui.components.ContentStickyHeader
import com.softyorch.taskapp.ui.components.dropDawnMenuCustom
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.contentColorAsSateAnimation
import com.softyorch.taskapp.utils.toStringFormatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(my_tasks),
                isMainScreen = true,
                nameScreen = AppScreens.MainScreen.name,
                navController = navController,
            )
        },
        floatingActionButton = {
            FABCustom()
        },
    ) {
        Content(
            it = it,
            viewModel = mainViewModel,
            navController = navController,
            configuration = LocalConfiguration.current
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Content(
    it: PaddingValues,
    viewModel: MainViewModel,
    navController: NavController,
    configuration: Configuration
) {

    val standardPadding = 8
    val maxHeight = configuration.screenHeightDp
    val maxWidth = configuration.screenWidthDp
    val splitWidth = maxWidth / 2 - (standardPadding + (standardPadding / 2))

    val modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        .padding(
            top = it.calculateTopPadding() + 8.dp,
            bottom = 8.dp,
            start = 8.dp,
            end = 8.dp
        )

    when (configuration.orientation) {
        ORIENTATION_LANDSCAPE -> {
            val modifierForLazy = Modifier
                .fillMaxHeight()
                .width(splitWidth.dp)
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.Top
            ) {
                OrientableContent(
                    modifier = modifierForLazy,
                    modifierForCheckedTasks = modifierForLazy,
                    viewModel,
                    navController
                )
            }
        }
        else -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Top
            ) {
                OrientableContent(
                    modifier = Modifier
                        .heightIn(min = 20.dp, max = (maxHeight / 2).dp)
                        .width(maxWidth.dp),
                    modifierForCheckedTasks = Modifier
                        .heightIn(min = 20.dp, max = (maxHeight).dp)
                        .width(maxWidth.dp),
                    viewModel,
                    navController
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun OrientableContent(
    modifier: Modifier,
    modifierForCheckedTasks: Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val taskListsChecked: List<TaskEntity> by viewModel.tasksEntityListChecked.observeAsState(
        initial = emptyList()
    )
    val taskListsUnchecked: List<TaskEntity> by viewModel.tasksEntityListUnchecked.observeAsState(
        initial = emptyList()
    )

    if (isLoading) CircularIndicatorCustomDialog(
        text = stringResource(loading_loading),
        modifier = Modifier
            .safeContentPadding()
    )

    var maxHeight = 0.1f
    taskListsUnchecked.count().let {
        if (it > 0) maxHeight = if (it < 8) (0.1f * it) + 0.1f else 0.8f
    }

    LazyColumnChecks(
        modifier = modifier,
        maxHeight = maxHeight,
        taskEntities = taskListsUnchecked,
        changeOrder = viewModel::changeOrderUncheckedTask,
        updateTaskEntity = viewModel::updateTask,
        enabled = !isLoading
    ) { navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}") }

    Spacer(modifier = Modifier.padding(4.dp))

    LazyColumnChecks(
        modifier = modifierForCheckedTasks.fillMaxHeight(),
        checkedOrNot = true,
        taskEntities = taskListsChecked,
        changeOrder = viewModel::changeOrderCheckedTask,
        updateTaskEntity = viewModel::updateTask,
        enabled = !isLoading
    ) { navController.navigate(AppScreensRoutes.DetailScreen.route + "/${it}") }
}

@ExperimentalMaterial3Api
@Composable
private fun RowInfoWithDropMenu(
    text: String,
    changeOrder: KFunction1<TaskOrder, Unit>
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RowInfo(text = text, paddingStart = 32.dp)
        dropDawnMenuCustom {
            changeOrder(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
private fun LazyColumnChecks(
    modifier: Modifier,
    maxHeight: Float = 0.9f,
    checkedOrNot: Boolean = false,
    taskEntities: List<TaskEntity>,
    changeOrder: KFunction1<TaskOrder, Unit>,
    updateTaskEntity: KSuspendFunction1<TaskEntity, Unit>,
    enabled: Boolean,
    onClick: (UUID) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()

    Column(
        modifier = modifier
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        RowInfoWithDropMenu(
            text = if (checkedOrNot) stringResource(tasks_completed_last_days) else stringResource(
                to_be_made
            ),
            changeOrder = changeOrder
        )
        if (taskEntities.isNotEmpty()) {
            val taskMap: Map<String, List<TaskEntity>> =
                taskEntities.groupBy { it.entryDate.toStringFormatDate() }

            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(maxHeight),
                    state = lazyState,
                    userScrollEnabled = true,
                    flingBehavior = ScrollableDefaults.flingBehavior()
                ) {

                    taskMap.forEach { (published, taskEntityList) ->
                        stickyHeader {
                            ContentStickyHeader(published = published)
                        }

                        items(taskEntityList) { task ->
                            var myCheck by remember { mutableStateOf(value = task.checkState) }

                            CheckCustom(
                                checked = myCheck,
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
                                enabled = enabled,
                                animated = true,
                                text = task.title
                            ) {
                                onClick(task.id)
                            }
                        }
                    }
                }

                val arrowUp by remember {
                    derivedStateOf {
                        lazyState.firstVisibleItemIndex > 0
                    }
                }
                val showIcon by remember {
                    derivedStateOf {
                        (lazyState.layoutInfo.visibleItemsInfo.count() <
                                lazyState.layoutInfo.totalItemsCount ||
                                lazyState.firstVisibleItemIndex > 0) ||
                                arrowUp
                    }
                }

                var onClickIcon by remember { mutableStateOf(value = false) }

                if (showIcon) {
                    Icon(
                        imageVector =
                        if (arrowUp) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(go_to_up),
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 8.dp)
                            .clickable {
                                onClickIcon = true
                                coroutineScope.launch {
                                    lazyState.let {
                                        lazyState.animateScrollToItem(
                                            index = if (it.firstVisibleItemIndex > 0) 0
                                            else it.layoutInfo.totalItemsCount
                                        )
                                    }
                                }
                            },
                        tint = onClickIcon.contentColorAsSateAnimation {
                            onClickIcon = false
                        }.value
                    )
                } else Box(modifier = Modifier.height(25.dp)) {}
            }
        } else RowInfo(
            text = if (checkedOrNot) stringResource(not_yet_complet_any_task) else stringResource(
                add_new_task
            ),
            paddingStart = 16.dp
        )
    }
}
