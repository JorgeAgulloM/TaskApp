package com.softyorch.taskapp.ui.screens.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun DetailScreen(
    navController: NavController,
    id: String
) {

    /** 16.09.2022
     * -Transformar a componente para probar a que se coloque encima de la pantalla main*/

    val viewModel = hiltViewModel<DetailScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()
    var enterDetails by remember { mutableStateOf(value = false) }
    var exitDetails by remember { mutableStateOf(value = false) }

    coroutineScope.launch {
        viewModel.getTask(id = id)
        delay(100)
        if (!exitDetails) enterDetails = true
    }

    val slideCheckBox by enterDetails.intOffsetAnimation {
        if (!enterDetails)
            navController.navigate(AppScreensRoutes.MainScreen.route) {
                popUpTo(AppScreensRoutes.DetailScreen.route) {
                    inclusive = true
                    navController.backQueue.clear()
                }
            }
    }
    val alphaAnimation: Float by exitDetails.alphaAnimation()
    val colorAnimation by exitDetails.containerColorAnimation()

    Column(
        modifier = Modifier
            .offset { slideCheckBox }
            .graphicsLayer(alpha = alphaAnimation)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    exitDetails = true
                    enterDetails = false
                },
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = colorAnimation, shape = MaterialTheme.shapes.large),
                //colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(
                        go_to_home
                    )
                )
            }
        }
        Content(viewModel = viewModel, navController = navController)
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Content(
    viewModel: DetailScreenViewModel,
    navController: NavController
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)

    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isDeleted: Boolean by viewModel.isDeleted.observeAsState(initial = false)
    val task: Task by viewModel.taskDetail.observeAsState(
        initial = Task(
            title = emptyString,
            description = emptyString,
            author = emptyString
        )
    )
    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }

    if (isLoading || isDeleted) {
        CircularIndicatorCustom(stringResource(loading_loading))
        if (isDeleted) LocalContext.current.toastError("La tarea ha sido eliminada") {}

    } else {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                )
        ) {

            RowInfoDetail(text = stringResource(details))
            ShowTaskDetails(task = task)
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))
            RowInfoDetail(text = task.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(top = 8.dp))
            TextDescriptionDetails(task = task)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                var openEditDialog by rememberSaveable { mutableStateOf(false) }
                var openCompleteDialog by rememberSaveable { mutableStateOf(false) }
                var openResetDialog by rememberSaveable { mutableStateOf(false) }
                var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

                ButtonCustomDetails(text = stringResource(edit_task), primary = true) {
                    openEditDialog = true
                }
                ButtonCustomDetails(
                    text = if (!task.checkState) stringResource(complete) else stringResource(
                        completed
                    ),
                    primary = true
                ) {
                    if (!task.checkState) openCompleteDialog = true
                    if (task.checkState) openResetDialog = true
                }
                ButtonCustomDetails(text = stringResource(delete)) { openDeleteDialog = true }

                /** Edit Dialog */
                if (openEditDialog) openEditDialog = newTaskDetails(
                    viewModel = viewModel, task = task
                )

                /** Complete Dialog */
                if (openCompleteDialog) AlertDialog(onDismissRequest = {
                    openCompleteDialog = false
                },
                    confirmButton = {
                        ButtonCustomDetails(text = stringResource(complete), primary = true) {
                            task.checkState = !task.checkState
                            task.finishDate = Date.from(Instant.now())
                            viewModel.updateTask(task = task)
                            navController.popBackStack()
                            navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
                            openCompleteDialog = false
                        }
                    },
                    text = {
                        TextDetails(
                            text = stringResource(great_next_one)
                        )
                    }
                )

                /** Reset Dialog */
                if (openResetDialog) AlertDialog(onDismissRequest = {
                    openResetDialog = false
                },
                    confirmButton = {
                        ButtonCustomDetails(text = stringResource(yes_modify_it), primary = true) {
                            /** OJO, se repite el código */
                            task.checkState = !task.checkState
                            task.finishDate = Date.from(Instant.now())
                            viewModel.updateTask(task = task)
                            navController.popBackStack()
                            navController.navigate(AppScreensRoutes.DetailScreen.route + "/${task.id}")
                            openResetDialog = false
                        }
                    },
                    dismissButton = {
                        ButtonCustomDetails(text = stringResource(cancel)) {
                            openResetDialog = false
                        }
                    },
                    text = {
                        TextDetails(
                            text = stringResource(sure_restart_task)
                        )
                    }
                )

                /** Delete Dialog */
                if (openDeleteDialog) AlertDialog(
                    onDismissRequest = {
                        openDeleteDialog = false
                    },
                    confirmButton = {
                        ButtonCustomDetails(text = stringResource(delete_it), primary = true) {
                            openDeleteDialog = false
                            viewModel.removeTask(task = task)
                            navController.navigate(AppScreensRoutes.MainScreen.route) {
                                navController.backQueue.clear()
                            }
                        }
                    },
                    dismissButton = {
                        ButtonCustomDetails(text = stringResource(cancel)) {
                            openDeleteDialog = false
                        }
                    },
                    text = {
                        TextDetails(
                            text = stringResource(you_sure_eliminate_task)
                        )
                    },
                )

            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
private fun newTaskDetails(
    viewModel: DetailScreenViewModel,
    task: Task,
): Boolean = newTask(
    addOrEditTaskFunc = viewModel::updateAndRefreshTask,
    userName = task.author,
    taskToEdit = task
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
    style: TextStyle = MaterialTheme.typography.titleMedium
) {
    RowInfo(
        text = text,
        paddingStart = 24.dp,
        style = style
    )
}

@Composable
private fun TextDescriptionDetails(
    task: Task
) {
    Text(
        text = task.description,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ShowTaskDetails(task: Task) {
    ShowTask(
        author = task.author,
        date = task.entryDate.toStringFormatted(),
        completedDate = task.finishDate?.toStringFormatted()
            ?: stringResource(unknown),
        paddingStart = 0.dp
    )
}

@Composable
private fun TextDetails(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )
}
