package com.softyorch.taskapp.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = hiltViewModel(),
    id: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Details",
                icon = Icons.Rounded.Details,
                nameScreen = TaskAppScreens.DetailsScreen.name,
                navController = navController,
            )
        }
    ) {
        Content(it = it, taskViewModel = taskViewModel, navController = navController, id = id)
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavController,
    id: String
) {

    produceState<Resource<Task>>(initialValue = Resource.Loading()) {
        value = taskViewModel.getTaskId(id = id)
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
                    RowIndication(task.title, fontSize = 20.sp, paddingStart = 24.dp)
                    Text(
                        text = task.description,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    RowIndication("Details", fontSize = 20.sp, paddingStart = 24.dp)
                    InfoTask(
                        author = task.author,
                        date = task.entryDate.toString(),
                        completedDate = task.finishDate.toString(),
                        paddingStart = 0.dp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        var openCompleteDialog by rememberSaveable { mutableStateOf(false) }

                        TaskButton(
                            onclick = {
                                task.checkState = !task.checkState
                                taskViewModel.updateTask(task = task)
                                openCompleteDialog = true
                            },
                            text = if (!task.checkState)
                                "Complete?"
                            else
                                "Completed",
                            primary = true
                        )

                        if (openCompleteDialog) {
                            AlertDialog(onDismissRequest = {
                                openCompleteDialog = false
                            },
                                confirmButton = {
                                    TaskButton(
                                        onclick = {
                                            navController.popBackStack()
                                            navController.navigate(TaskAppScreens.DetailsScreen.name + "/${task.id}")
                                            openCompleteDialog = false
                                        },
                                        text = "OK",
                                        primary = true
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Great, one less task. On to the next one...",
                                        textAlign = TextAlign.Center
                                    )
                                })
                        }

                        var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                        TaskButton(
                            onclick = {
                                openDeleteDialog = true
                            },
                            text = "Delete"
                        )

                        if (openDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    openDeleteDialog = false
                                },
                                confirmButton = {
                                    TaskButton(
                                        onclick = {
                                            openDeleteDialog = false
                                            taskViewModel.removeTask(task = task)
                                            navController.navigate(TaskAppScreens.MainScreen.name)
                                        }, "Delete it", true
                                    )
                                },
                                dismissButton = {
                                    TaskButton(
                                        onclick = {
                                            openDeleteDialog = false
                                        }, "Cancel"
                                    )
                                },
                                //title = { Text(text = "Eliminar tarea") },
                                text = {
                                    Text(
                                        text = "Are you sure you want to eliminate the task?",
                                        textAlign = TextAlign.Center
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
}