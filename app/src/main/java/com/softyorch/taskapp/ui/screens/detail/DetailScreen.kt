package com.softyorch.taskapp.ui.screens.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.CheckCustom
import com.softyorch.taskapp.ui.components.CircularIndicatorCustomDialog
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.*
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
    val slideHead by enterDetails.intOffsetAnimationTransition(durationMillis = 100) {}
    val slideCheckBox by enterDetails.intOffsetAnimationTransition {
        if (!enterDetails)
            navController.popBackStack()
    }
    val alphaAnimation: Float by exitDetails.alphaAnimation {}
    val colorAnimation by exitDetails.containerColorAnimation()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .graphicsLayer(alpha = alphaAnimation)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().offset { slideHead },
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
                text = stringResource(details),
                paddingStart = 30.dp,
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (isLoading || isDeleted) {
            CircularIndicatorCustomDialog(stringResource(loading_loading))
            if (isDeleted) LocalContext.current.toastError(stringResource(task_deleted)) {}
        }
        Content(viewModel, navController, Modifier.offset { slideCheckBox })
    }
}

@ExperimentalMaterial3Api
@Composable
private fun Content(
    viewModel: DetailScreenViewModel,
    navController: NavController,
    modifier: Modifier
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)

    val task: TaskModelUi by viewModel.taskEntityDetail.observeAsState(
        initial = TaskModelUi(
            title = emptyString,
            description = emptyString,
            author = emptyString
        )
    )
    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        ShowTaskDetails(task = task)
        Divider(modifier = Modifier.padding(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            var openEditDialog by rememberSaveable { mutableStateOf(false) }
            var openCompleteDialog by rememberSaveable { mutableStateOf(false) }
            var openResetDialog by rememberSaveable { mutableStateOf(false) }
            var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

            RowInfoDetail(
                text = task.title,
                isFinish = task.checkState,
                onEdit = { openEditDialog = true },
                onDelete = { openDeleteDialog = true }
            ) {
                if (!task.checkState) openCompleteDialog = true
                if (task.checkState) openResetDialog = true
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            TextDescriptionDetails(description = task.description)

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
                        viewModel.updateTask(taskModelUi = task)
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
                        task.finishDate = null
                        viewModel.updateTask(taskModelUi = task)
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
                        viewModel.removeTask(taskModelUi = task)
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
    task: TaskModelUi,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RowInfoDetail(
    text: String,
    isFinish: Boolean = false,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var onClickEdit by remember { mutableStateOf(value = false) }
        val colorStateEdit by onClickEdit.editIconColorChangeOnClick {
            onClickEdit = false
            onEdit()
        }
        var onClickDelete by remember { mutableStateOf(value = false) }
        val colorStateDelete by onClickDelete.deleteIconColorChangeOnClick {
            onClickDelete = false
            onDelete()
        }
        CheckCustom(
            checked = isFinish,
            onCheckedChange = { onClick() },
            text = text
        ) {}
        Row(
            modifier = Modifier.safeContentPadding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .background(color = Color.Transparent, shape = MaterialTheme.shapes.large)
                    .clickable { onClickEdit = true },
                imageVector = Icons.Rounded.ModeEdit,
                contentDescription = "Edit task",
                tint = colorStateEdit
            )
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .background(color = Color.Transparent, shape = MaterialTheme.shapes.large)
                    .clickable { onClickDelete = true },
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete task",
                tint = colorStateDelete
            )
        }
    }
}

@Composable
private fun TextDescriptionDetails(
    description: String
) {
    val coroutineScope = rememberCoroutineScope()
    val columnStart = 0
    val splitColumn = 4
    val ratio = 20
    val scrollState = rememberScrollState(initial = columnStart)
    val showArrow by remember { derivedStateOf { scrollState.maxValue > columnStart } }
    val arrowUp by remember { derivedStateOf { scrollState.value >= (scrollState.maxValue - ratio) } }
    var onClickIcon by remember { mutableStateOf(value = false) }
    val animatedTint by onClickIcon.editIconColorChangeOnClick {
        onClickIcon = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight(0.90f)
                .verticalScroll(state = scrollState)
        ) {
            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (showArrow) Icon(
            if (arrowUp) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
            contentDescription = stringResource(go_to_up),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = Color.Transparent, shape = MaterialTheme.shapes.large)
                .padding(top = 8.dp)
                .clickable {
                    onClickIcon = true
                    coroutineScope.launch {
                        scrollState.animateScrollTo(
                            if (arrowUp) columnStart
                            else if ((scrollState.value + scrollState.maxValue / splitColumn) < scrollState.maxValue) {
                                scrollState.value + scrollState.maxValue / splitColumn
                            } else scrollState.maxValue
                        )
                    }
                },
            tint = animatedTint
        )
    }
}

@Composable
private fun ShowTaskDetails(task: TaskModelUi) {
    ShowTask(
        author = task.author,
        date = task.entryDate.toStringFormatted(),
        completedDate = task.finishDate?.toStringFormatted()
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
