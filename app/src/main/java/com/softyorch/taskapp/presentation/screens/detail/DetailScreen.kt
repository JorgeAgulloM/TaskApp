package com.softyorch.taskapp.presentation.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.toStringFormatted
import com.softyorch.taskapp.presentation.widgets.RowInfo
import com.softyorch.taskapp.presentation.widgets.ShowTask
import com.softyorch.taskapp.presentation.widgets.newTask.newTask
import com.softyorch.taskapp.utils.StandardizedSizes
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
                    RowInfoDetail(text = task.title, textSizes = textSizes)
                    TextDescriptionDetails(task, textSizes)
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    RowInfoDetail(text = "Details", textSizes = textSizes)
                    ShowTaskDetails(task)

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

                        ButtonCustomDetails(text = "Edit task", primary = true) {
                            openEditDialog = true
                        }
                        ButtonCustomDetails(
                            text = if (!task.checkState) "Complete?" else "Completed",
                            primary = true
                        ) {
                            task.checkState = !task.checkState
                            task.finishDate = Date.from(Instant.now())
                            viewModel.updateTask(task = task)
                            openCompleteDialog = true
                        }
                        ButtonCustomDetails(text = "Delete") { openDeleteDialog = true }

                        if (openEditDialog) openEditDialog = newTaskDetails(
                            viewModel = viewModel, task = task, textSizes = textSizes
                        )

                        if (openCompleteDialog) AlertDialog(onDismissRequest = {
                            openCompleteDialog = false
                        },
                            confirmButton = {
                                ButtonCustomDetails(text = "OK", primary = true) {
                                    navController.popBackStack()
                                    navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
                                    openCompleteDialog = false
                                }
                            },
                            text = {
                                TextDetails(
                                    text = "Great, one less task. On to the next one...",
                                    fontSize = textSizes.normalSize
                                )
                            }
                        )

                        if (openDeleteDialog) AlertDialog(
                            onDismissRequest = {
                                openDeleteDialog = false
                            },
                            confirmButton = {
                                ButtonCustomDetails(text = "Delete it", primary = true) {
                                    openDeleteDialog = false
                                    viewModel.removeTask(task = task)
                                    navController.navigate(AppScreens.MainScreen.name)
                                }
                            },
                            dismissButton = {
                                ButtonCustomDetails(text = "Cancel") {
                                    openDeleteDialog = false
                                }
                            },
                            text = {
                                TextDetails(
                                    text = "Are you sure you want to eliminate the task?",
                                    fontSize = textSizes.normalSize
                                )
                            },
                        )

                    }
                }
            }
        }
}

@Composable
private fun newTaskDetails(
    viewModel: DetailScreenViewModel,
    task: Task,
    textSizes: StandardizedSizes
): Boolean = newTask(
    addOrEditTaskFunc = viewModel::updateTask,
    userName = task.author,
    taskToEdit = task,
    textSizes = textSizes
)

@Composable
private fun ButtonCustomDetails(
    text: String,
    primary: Boolean = false,
    onClick: () -> Unit
) {
    ButtonCustom(
        onClick = { onClick() },
        text = text,
        primary = primary
    )
}

@Composable
private fun RowInfoDetail(
    text: String,
    textSizes: StandardizedSizes
) {
    RowInfo(
        text = text,
        paddingStart = 24.dp,
        textSizes = textSizes
    )
}

@Composable
private fun TextDescriptionDetails(
    task: Task,
    textSizes: StandardizedSizes
) {
    Text(
        text = task.description,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = textSizes.normalSize
    )
}

@Composable
private fun ShowTaskDetails(task: Task) {
    ShowTask(
        author = task.author,
        date = task.entryDate.toStringFormatted(),
        completedDate = task.finishDate?.toStringFormatted()
            ?: "Unknown",
        paddingStart = 0.dp
    )
}

@Composable
private fun TextDetails(text: String, fontSize: TextUnit) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = fontSize
    )
}
