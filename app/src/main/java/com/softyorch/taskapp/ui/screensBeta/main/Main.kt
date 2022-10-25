/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomFakeNavigationBar
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomNavItem
import com.softyorch.taskapp.ui.screensBeta.main.components.BottomSheetCustom
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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