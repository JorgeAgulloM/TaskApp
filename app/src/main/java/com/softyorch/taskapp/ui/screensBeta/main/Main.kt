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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.upDownIntegerAnimated
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBeta(navController: NavController) {
    val viewModel = hiltViewModel<MainViewModel>()
    val items = BottomNavItem.items
    var index by remember { mutableStateOf(value = 0) }
    when (index) {
        0 -> viewModel.load(TaskLoad.UncheckedTask)
        1 -> viewModel.load(TaskLoad.CheckedTask)
        2 -> viewModel.load(TaskLoad.AllTask)
    }
    val taskList: List<TaskModelUi> by viewModel.tasks.observeAsState(listOf(TaskModelUi.emptyTask))
    val isVisible: Boolean by viewModel.isVisible.observeAsState(initial = false)

    Scaffold(
        topBar = { SmallTopAppBarCustom(true, items[index].name, navController) },
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
        Body(taskList, isVisible, it) { task -> viewModel.updateTask(task) }
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
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.onTertiary
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = contentBrush, shape = MaterialTheme.shapes.large
            )
            .padding(
                start = 4.dp,
                end = 4.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomSheetCustom(taskList, isVisible, pv) { onCheckedChange(it) }
    }
}

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
                .padding(top = paddingValues.calculateTopPadding())
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large
                )
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier,
                    state = lazyState,
                    verticalArrangement = Arrangement.Top
                ) {
                    items(taskList) { task ->
                        CardCustom(task) {
                            onCheckedChange(
                                task.copy(
                                    checkState = it,
                                    finishDate = Date.from(Instant.now())
                                )
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardCustom(
    task: TaskModelUi,
    onCheckedChange: (Boolean) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    val height by isOpen.upDownIntegerAnimated(200, 65)

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(height.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = ELEVATION_DP),
        border = BorderStroke(0.5.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isOpen = !isOpen
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            CheckCustom(
                checked = task.checkState,
                onCheckedChange = {
                    onCheckedChange(it)
                },
                enabled = true,
                animated = false,
                text = task.title,
            ) {}
            Column(modifier = Modifier.verticalScroll(rememberScrollState(), enabled = isOpen)) {
                Text(
                    text = task.description,
                    overflow = if (isOpen) TextOverflow.Visible else TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

data class BottomNavItem(
    var indexId: Int,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
) {
    companion object {
        val items = listOf(
            BottomNavItem(
                0, "To do", Icons.Rounded.CheckBoxOutlineBlank, 0
            ),
            BottomNavItem(
                1, "Finished", Icons.Rounded.CheckBox, 5
            ),
            BottomNavItem(
                2, "History", Icons.Rounded.History, 10
            )
        )
    }
}