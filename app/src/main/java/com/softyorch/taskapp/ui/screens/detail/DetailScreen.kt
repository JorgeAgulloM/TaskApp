package com.softyorch.taskapp.ui.screens.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.ui.components.CircularIndicatorCustomDialog
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

    val viewModel = hiltViewModel<DetailScreenViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val isDeleted: Boolean by viewModel.isDeleted.observeAsState(initial = false)

    val coroutineScope = rememberCoroutineScope()
    var enterDetails by remember { mutableStateOf(value = false) }
    var exitDetails by remember { mutableStateOf(value = false) }

    if (!exitDetails) coroutineScope.launch {
        viewModel.getTask(id = id)
        delay(100)
         enterDetails = true
    }

    val slideCheckBox by enterDetails.intOffsetAnimationTransition {
        if (!enterDetails)
            navController.popBackStack()
    }
    val alphaAnimation: Float by exitDetails.alphaAnimation{}
    val colorAnimation by exitDetails.containerColorAnimation()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .offset { slideCheckBox }
            .graphicsLayer(alpha = alphaAnimation)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    exitDetails = true
                    enterDetails = false
                },
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
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
            RowInfo(
                text = "Detalles",
                paddingStart = 30.dp,
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (isLoading || isDeleted) {
            CircularIndicatorCustomDialog(stringResource(loading_loading))
            if (isDeleted) LocalContext.current.toastError("La tarea ha sido eliminada") {}
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

    val taskEntity: TaskEntity by viewModel.taskEntityDetail.observeAsState(
        initial = TaskEntity(
            title = emptyString,
            description = emptyString,
            author = emptyString
        )
    )
    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        RowInfoDetail(text = stringResource(details))
        ShowTaskDetails(taskEntity = taskEntity)
        Divider(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 16.dp
            )
        )
        RowInfoDetail(
            text = taskEntity.title,
            isFinish = taskEntity.checkState,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        TextDescriptionDetails(taskEntity = taskEntity)

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
                text = if (!taskEntity.checkState) stringResource(complete) else stringResource(
                    completed
                ),
                primary = true
            ) {
                if (!taskEntity.checkState) openCompleteDialog = true
                if (taskEntity.checkState) openResetDialog = true
            }
            ButtonCustomDetails(text = stringResource(delete)) { openDeleteDialog = true }

            /** Edit Dialog */
            if (openEditDialog) openEditDialog = newTaskDetails(
                viewModel = viewModel, taskEntity = taskEntity
            )

            /** Complete Dialog */
            if (openCompleteDialog) AlertDialog(onDismissRequest = {
                openCompleteDialog = false
            },
                confirmButton = {
                    ButtonCustomDetails(text = stringResource(complete), primary = true) {
                        taskEntity.checkState = !taskEntity.checkState
                        taskEntity.finishDate = Date.from(Instant.now())
                        viewModel.updateTask(taskEntity = taskEntity)
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
                        taskEntity.checkState = !taskEntity.checkState
                        taskEntity.finishDate = null
                        viewModel.updateTask(taskEntity = taskEntity)
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
                        viewModel.removeTask(taskEntity = taskEntity)
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


@ExperimentalMaterial3Api
@Composable
private fun newTaskDetails(
    viewModel: DetailScreenViewModel,
    taskEntity: TaskEntity,
): Boolean = newTask(
    addOrEditTaskEntityFunc = viewModel::updateAndRefreshTask,
    userName = taskEntity.author,
    taskEntityToEdit = taskEntity
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
    isFinish: Boolean = false,
    style: TextStyle = MaterialTheme.typography.titleMedium
) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (isFinish) Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Task finished",
            tint = MaterialTheme.colorScheme.primary
        )
        else Box(modifier = Modifier.size(24.dp)) {}
        RowInfo(
            text = text,
            paddingStart = 4.dp,
            style = style
        )
    }
}

@Composable
private fun TextDescriptionDetails(
    taskEntity: TaskEntity
) {
    Text(
        text = taskEntity.description,
        modifier = Modifier.padding(start = 16.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ShowTaskDetails(taskEntity: TaskEntity) {
    ShowTask(
        author = taskEntity.author,
        date = taskEntity.entryDate.toStringFormatted(),
        completedDate = taskEntity.finishDate?.toStringFormatted()
            ?: emptyString,
        paddingStart = 8.dp
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
