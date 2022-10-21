/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.ContentStickyHeader
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.ui.screensBeta.main.components.CardTaskCustom
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.toStringFormatDate
import com.softyorch.taskapp.utils.extensions.toStringFormatted
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    val viewModel = hiltViewModel<MainViewModel>()
    val items = listOf(
        BottomNavItem(
            0, stringResource(R.string.to_be_made), Icons.Rounded.EditNote, 0
        ),
        BottomNavItem(
            1, stringResource(R.string.tasks_completed), Icons.Rounded.Checklist, 5
        ),
        BottomNavItem(
            2, stringResource(R.string.history), Icons.Rounded.History, 10
        )
    )
    var index by remember { mutableStateOf(value = 0) }
    when (index) {
        0 -> viewModel.load(TaskLoad.UncheckedTask)
        1 -> viewModel.load(TaskLoad.CheckedTask)
        2 -> viewModel.load(TaskLoad.AllTask)
    }
    val taskList: List<TaskModelUi> by viewModel.tasks.observeAsState(listOf(TaskModelUi.emptyTask))
    val isVisible: Boolean by viewModel.isVisible.observeAsState(initial = false)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        topBar = {
            SmallTopAppBarCustom(
                true,
                items[index].name,
                navController,
                icon = items[index].icon
            )
        },
        bottomBar = {
            BottomFakeNavigationBar(
                index = index,
                items = items,
                onItemClick = { itemButton ->
                    items.forEach { item ->
                        if (item.indexId == itemButton.indexId)
                            index = item.indexId
                    }
                }
            )
        },
        floatingActionButton = { FABCustom() },
        floatingActionButtonPosition = FabPosition.End,
        contentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) {
        Body(taskList, isVisible, it) { task ->
            scope.launch {
                viewModel.updateTask(task)
            }
        }
    }
}

@Composable
fun Body(
    taskList: List<TaskModelUi>,
    isVisible: Boolean,
    pv: PaddingValues,
    onCheckedChange: (TaskModelUi) -> Unit
) {

    val contentBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = contentBrush)
            .padding(
                start = 4.dp,
                end = 4.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomSheetCustom(taskList, isVisible, pv) { onCheckedChange(it) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomSheetCustom(
    taskList: List<TaskModelUi>,
    isVisible: Boolean,
    paddingValues: PaddingValues,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    val lazyState = rememberLazyListState()
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.secondary
        )
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = SHEET_TRANSITION_ENTER,
        exit = SHEET_TRANSITION_EXIT
    ) {
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large.copy(
                        topStart = CornerSize(3.dp),
                        topEnd = CornerSize(50.dp)
                    )
                )
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (taskList.isNotEmpty()) {
                    val taskMap: Map<String, List<TaskModelUi>> =
                        taskList.groupBy { it.entryDate.toStringFormatDate() }
                    LazyColumn(
                        modifier = Modifier,
                        state = lazyState,
                        verticalArrangement = Arrangement.Top
                    ) {

                        taskMap.forEach { (published, taskEntityList) ->
                            stickyHeader {
                                ContentStickyHeader(published = published)
                            }

                            items(taskEntityList) { task ->
                                CardTaskCustom(task, isVisible) {
                                    onCheckedChange(it)
                                }
                            }
                        }
                        item { Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {} }
                    }

                } else Text(text = stringResource(R.string.add_new_task))
            }
        }
    }
}

@Composable
fun ShowTaskDetails(task: TaskModelUi) {
    ShowTask(
        author = task.author,
        date = task.entryDate.toStringFormatted(),
        completedDate = task.finishDate?.toStringFormatted()
            ?: emptyString,
        paddingStart = 8.dp
    )
}

data class BottomNavItem(
    var indexId: Int,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
)