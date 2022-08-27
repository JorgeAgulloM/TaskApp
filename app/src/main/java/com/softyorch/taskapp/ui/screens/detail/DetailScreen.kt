package com.softyorch.taskapp.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.utils.toStringFormatted
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import java.time.Instant
import java.util.*


@ExperimentalMaterial3Api
@Composable
fun DetailScreen(
    navController: NavController,
    id: String
) {

    val viewModel = hiltViewModel<DetailScreenViewModel>()

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Details",
                nameScreen = AppScreens.DetailsScreen.name,
                navController = navController,
            )
        }
    ) {
        Content(it = it, viewModel = viewModel, navController = navController, id = id)
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    viewModel: DetailScreenViewModel,
    navController: NavController,
    id: String
) {

    val textSizes = viewModel.sizeSelectedOfUser()

    produceState<Resource<Task>>(initialValue = Resource.Loading()) {
        value = viewModel.getTaskId(id = id)
    }.value
        .let { data ->
            data.data?.let { task ->
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(
                            top = it.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                ) {
                    RowInfo(
                        task.title,
                        paddingStart = 24.dp,
                        textSizes = textSizes
                    )
                    Text(
                        text = task.description,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = textSizes.normalSize
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    RowInfo(
                        "Details",
                        paddingStart = 24.dp,
                        textSizes = textSizes
                    )
                    ShowTask(
                        author = task.author,
                        date = task.entryDate.toStringFormatted(task.entryDate),
                        completedDate = task.finishDate?.toStringFormatted(task.finishDate!!)
                            ?: "Unknown",
                        paddingStart = 0.dp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        var openEditDialog by rememberSaveable { mutableStateOf(false) }
                        var openCompleteDialog by rememberSaveable { mutableStateOf(false) }
                        var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                        ButtonCustom(
                            onClick = {
                                openEditDialog = true
                            },
                            text = "Edit task",
                            primary = true
                        )

                        ButtonCustom(
                            onClick = {
                                task.checkState = !task.checkState
                                task.finishDate = Date.from(Instant.now())
                                viewModel.updateTask(task = task)
                                openCompleteDialog = true
                            },
                            text = if (!task.checkState)
                                "Complete?"
                            else
                                "Completed",
                            primary = true
                        )

                        ButtonCustom(
                            onClick = {
                                openDeleteDialog = true
                            },
                            text = "Delete"
                        )

                        if (openEditDialog) {
                            openEditDialog = newTask(
                                addOrEditTaskFunc = viewModel::updateTask,
                                userName = viewModel.nameOfUserLogin(),
                                taskToEdit = task,
                                textSizes = textSizes
                            )
                        }

                        if (openCompleteDialog) {
                            AlertDialog(onDismissRequest = {
                                openCompleteDialog = false
                            },
                                confirmButton = {
                                    ButtonCustom(
                                        onClick = {
                                            navController.popBackStack()
                                            navController.navigate(AppScreens.DetailsScreen.name + "/${task.id}")
                                            openCompleteDialog = false
                                        },
                                        text = "OK",
                                        primary = true
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Great, one less task. On to the next one...",
                                        textAlign = TextAlign.Center,
                                        fontSize = textSizes.normalSize
                                    )
                                })
                        }

                        if (openDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    openDeleteDialog = false
                                },
                                confirmButton = {
                                    ButtonCustom(
                                        onClick = {
                                            openDeleteDialog = false
                                            viewModel.removeTask(task = task)
                                            navController.navigate(AppScreens.MainScreen.name)
                                        }, "Delete it", true
                                    )
                                },
                                dismissButton = {
                                    ButtonCustom(
                                        onClick = {
                                            openDeleteDialog = false
                                        }, "Cancel"
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Are you sure you want to eliminate the task?",
                                        textAlign = TextAlign.Center,
                                        fontSize = textSizes.normalSize
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
}