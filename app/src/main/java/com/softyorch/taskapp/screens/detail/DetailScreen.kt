package com.softyorch.taskapp.screens.detail

import androidx.compose.foundation.layout.*
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
import com.softyorch.taskapp.components.ButtonCustom
import com.softyorch.taskapp.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.utils.*


@ExperimentalMaterial3Api
@Composable
fun DetailScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = hiltViewModel(),
    id: String
) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "Details",
                nameScreen = AppScreens.DetailsScreen.name,
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
                    RowInfo(task.title, fontSize = 20.sp, paddingStart = 24.dp)
                    Text(
                        text = task.description,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    RowInfo("Details", fontSize = 20.sp, paddingStart = 24.dp)
                    ShowTask(
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

                        ButtonCustom(
                            onClick = {
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
                                        textAlign = TextAlign.Center
                                    )
                                })
                        }

                        var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                        ButtonCustom(
                            onClick = {
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
                                    ButtonCustom(
                                        onClick = {
                                            openDeleteDialog = false
                                            taskViewModel.removeTask(task = task)
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