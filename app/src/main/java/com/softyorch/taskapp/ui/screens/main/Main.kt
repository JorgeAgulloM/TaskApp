/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
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
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.FABCustom
import com.softyorch.taskapp.ui.screens.main.components.common.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.screens.main.components.bottomMenu.BottomFakeNavigationBar
import com.softyorch.taskapp.ui.screens.main.components.bottomMenu.BottomNavItem
import com.softyorch.taskapp.ui.screens.main.components.main.BottomSheetCustom
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
    val stateMain: StateMain by viewModel.stateMain.observeAsState(initial = StateMain.Main)
    val isVisible: Boolean by viewModel.isVisible.observeAsState(initial = false)
    val scope = rememberCoroutineScope()

    val topColor = animateColorAsState(
        targetValue = when (stateMain) {
            StateMain.Main -> MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
            StateMain.NewTask -> MaterialTheme.colorScheme.primaryContainer
            StateMain.Settings -> MaterialTheme.colorScheme.primaryContainer
            StateMain.UserData -> MaterialTheme.colorScheme.onTertiary
        },
        animationSpec = spring(dampingRatio = 3f)
    )

    val bottomColor = animateColorAsState(
        targetValue = when (stateMain) {
            StateMain.Main -> MaterialTheme.colorScheme.background
            StateMain.NewTask -> MaterialTheme.colorScheme.primary
            StateMain.Settings -> MaterialTheme.colorScheme.primary
            StateMain.UserData -> MaterialTheme.colorScheme.tertiary
        },
        animationSpec = spring(dampingRatio = 3f)
    )

    val contentBrush = Brush.verticalGradient(
        colors = listOf(
            topColor.value,
            bottomColor.value
        )
    )


    Scaffold(
        modifier = Modifier,
        topBar = {
            SmallTopAppBarCustom(
                items[index].name,
                icon = items[index].icon,
                state = stateMain,
                scope = {
                    viewModel.changeState(it)
                }
            )
        },
        bottomBar = {
            BottomFakeNavigationBar(
                index = index,
                items = items,
                isEnabled = stateMain == StateMain.Main,
                settings = stateMain == StateMain.Settings, //pasar a true para mostrar las settings
                userData = stateMain == StateMain.UserData,
                navController = navController,
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
            ) {
                viewModel.changeState(StateMain.Main)
            }
        },
        floatingActionButton = {
            FABCustom(
                hide = stateMain == StateMain.Settings || stateMain == StateMain.UserData
            ) {

                if (it) viewModel.changeState(StateMain.Main)
                else viewModel.changeState(StateMain.NewTask)

            }
        },
        floatingActionButtonPosition = FabPosition.End,
        contentColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
    ) {
        Body(taskList, isVisible, index, contentBrush, {
            viewModel.delete(it)
        }
        ) { task ->
            scope.launch {
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
    contentBrush: Brush,
    deleteTask: (TaskModelUi) -> Unit,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = contentBrush
                //color = MaterialTheme.colorScheme.background
            )
            .padding(
                start = 4.dp,
                end = 4.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomSheetCustom(taskList, isVisible, index, { deleteTask(it) }) { onCheckedChange(it) }
    }
}