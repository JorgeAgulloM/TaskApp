package com.softyorch.taskapp.presentation.screens.detail

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.RowInfo
import com.softyorch.taskapp.presentation.widgets.ShowTask
import com.softyorch.taskapp.presentation.widgets.newTask.newTask
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*


@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun DetailScreen(
    navController: NavController,
    id: String
) {

    val viewModel = hiltViewModel<DetailScreenViewModel>()
    val coroutineScope = rememberCoroutineScope()

    var visibleScreen by remember { mutableStateOf(value = false) }
    rememberCoroutineScope().launch {
        delay(TIME_IN_MILLIS_OF_DELAY)
        visibleScreen = true
    }

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(details),
                nameScreen = AppScreens.DetailsScreen.name,
                navController = navController,
            ) {
                visibleScreen = false
            }
        }
    ) {
        AnimatedVisibility(
            visible = visibleScreen,
            enter = ANIMATED_ENTER,
            exit = ANIMATED_EXIT
        ) {
            coroutineScope.launch { viewModel.getTask(id = id) }
            Content(it = it, viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    viewModel: DetailScreenViewModel,
    navController: NavController
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)

    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val task: Task by viewModel.taskDetail.observeAsState(
        initial = Task(
            title = emptyString,
            description = emptyString,
            author = emptyString
        )
    )

    if (isLoading) CircularIndicatorCustom(stringResource(loading_loading))
    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(
                top = it.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
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
