/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.ContentStickyHeader
import com.softyorch.taskapp.ui.components.DividerCustom
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.ui.screensBeta.main.components.textTransform
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.toStringFormatDate
import com.softyorch.taskapp.utils.extensions.toStringFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    scope.launch {
                        viewModel.visible()
                        delay(400)
                        items.forEach { item ->
                            if (item.indexId == itemButton.indexId)
                                index = item.indexId
                        }
                    }
                }
            )
        },
        floatingActionButton = { FABCustom() },
        floatingActionButtonPosition = FabPosition.End,
        contentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) {
        Body(taskList, isVisible, index) { task ->
            scope.launch {
                //viewModel.dropTaskLocalList(task)
                viewModel.updateTasks(task)
            }
        }
    }
}

@Composable
fun Body(
    taskList: List<TaskModelUi>,
    isVisible: Boolean,
    index: Int,
    onCheckedChange: (TaskModelUi) -> Unit
) {

    val contentBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondary
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
        BottomSheetCustom(taskList, isVisible, index) { onCheckedChange(it) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomSheetCustom(
    taskList: List<TaskModelUi>,
    isVisible: Boolean,
    index: Int,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = SHEET_TRANSITION_ENTER,
        exit = SHEET_TRANSITION_EXIT
    ) {
        Column(
            modifier = Modifier
                .padding(top = (SMALL_TOP_BAR_HEIGHT + 8).dp)
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large.copy(
                        topStart = CornerSize(3.dp),
                        topEnd = CornerSize(50.dp)
                    )
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (taskList.isNotEmpty()) {
                    //val list by remember { mutableStateOf(taskList) }
                    val list = taskList

                    val taskMap: Map<String, List<TaskModelUi>> =
                        list.filter {
                            when (index) {
                                0 -> {
                                    !it.checkState
                                }
                                1 -> {
                                    it.checkState
                                }
                                else -> it.title.length > 1
                            }
                        }.groupBy { it.entryDate.toStringFormatDate() }
                    Log.d("LAZYCOLUMN", "LIST -> $list")

                    LazyColumn(
                        modifier = Modifier,
                        state = lazyState,
                        verticalArrangement = Arrangement.Top,
                        content = {
                            taskMap.forEach { (published, taskEntityList) ->
                                stickyHeader {
                                    ContentStickyHeader(published = published)
                                }

                                items(taskEntityList) { task ->
                                    ElevatedCardCustom(task, scope) { onCheckedChange(it) }
                                }
                            }
                            item { Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {} }
                        }
                    )
                } else Text(text = stringResource(R.string.add_new_task))
            }
        }
    }
}

@Composable
private fun ElevatedCardCustom(
    task: TaskModelUi,
    scope: CoroutineScope,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    isChecked = task.checkState

    ElevatedCard(
        modifier = Modifier.padding(
            vertical = 2.dp,
            horizontal = 4.dp
        ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        isOpen = !isOpen
                    },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = SHEET_TRANSITION_ENTER,
                        exit = SHEET_TRANSITION_EXIT
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ShowTaskDetails(task)
                            DividerCustom(16.dp, 4.dp)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(
                                (-8).dp, (-8).dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        content = {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { click ->
                                    scope.launch {
                                        isChecked = click
                                        delay(300)
                                        onCheckedChange(
                                            task.copy(
                                                checkState = click,
                                                finishDate = if (click) Date.from(
                                                    Instant.now()
                                                ) else null
                                            )
                                        )
                                    }
                                }
                            )
                            Text(
                                text = task.title,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = if (task.checkState) MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.outline,
                                    textDecoration = TextDecoration.LineThrough,
                                ) else MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )

                    var isMinCollapse by remember {
                        mutableStateOf(
                            value = false
                        )
                    }

                    val lines = 3
                    val maxTextLength = 40
                    val lineHeight = 20
                    isMinCollapse =
                        task.description.length / maxTextLength > lines

                    Text(
                        text = textTransform(
                            isOpen,
                            task.description,
                            isMinCollapse,
                            maxTextLength
                        ),
                        modifier = Modifier
                            .offset(0.dp, (-16).dp)
                            .padding(
                                start = 8.dp,
                                end = 4.dp
                            ),
                        lineHeight = lineHeight.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    )
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