package com.softyorch.taskapp.presentation.widgets.newTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.widgets.ShowTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.toStringFormatted
import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1

@Composable
fun newTask(
    addOrEditTaskFunc: KFunction1<Task, Job>,
    userName: String,
    taskToEdit: Task?
): Boolean {

    val viewModel = hiltViewModel<NewTaskViewModel>()
    val title: String by viewModel.title.observeAsState(initial = taskToEdit?.title ?: emptyString)
    val description: String by viewModel.description.observeAsState(
        initial = taskToEdit?.description ?: emptyString
    )
    val saveTaskEnable: Boolean by viewModel.saveTaskEnable.observeAsState(initial = taskToEdit != null)

    var openDialog by remember { mutableStateOf(true) }
    val dateCreatedFormatted =
        taskToEdit?.entryDate?.toStringFormatted() ?: Date.from(Instant.now()).toStringFormatted()
    val dateCompletedFormatted =
        taskToEdit?.finishDate?.toStringFormatted() ?: stringResource(unknown)

    Dialog(
        onDismissRequest = {
            openDialog = false
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(1.1f)
                    .fillMaxHeight(0.72f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    ),
                content = {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ShowTaskNewTask(
                            userName = userName, dateFormatted = dateCreatedFormatted,
                            dateCompletedFormatted = dateCompletedFormatted
                        )
                        TextFieldCustomNewTaskName(text = title) {
                            viewModel.onTextFieldChanged(title = it, description = description)
                        }
                        TextFieldCustomNewTaskDescription(
                            text = description,
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    /**Revisar esto, duplica el código*/
                                    if (saveTaskEnable) {
                                        addOrEditTaskFunc(
                                            taskToEditOrNewTask(
                                                taskToEdit = taskToEdit, title = title,
                                                description = description, userName = userName
                                            )
                                        )
                                        viewModel.onResetValues()
                                        openDialog = false
                                    }
                                }
                            )
                        ) {
                            viewModel.onTextFieldChanged(title = title, description = it)
                        }

                        Column(
                            modifier = Modifier.width(width = 300.dp).padding(top = 16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ButtonCustomNewTask(
                                taskToEdit = taskToEdit, enable = saveTaskEnable
                            ) {
                                addOrEditTaskFunc(
                                    taskToEditOrNewTask(
                                        taskToEdit = taskToEdit, title = title,
                                        description = description, userName = userName
                                    )
                                )
                                viewModel.onResetValues()
                                openDialog = false
                            }
                            ButtonCustom(onClick = { openDialog = false }, text = stringResource(
                                cancel
                            ))
                        }
                    }
                }
            )
        }
    )
    return openDialog
}

private fun taskToEditOrNewTask(
    taskToEdit: Task?,
    title: String,
    description: String,
    userName: String
): Task = (taskToEdit?.copy(
    title = title,
    description = description
) ?: Task(
    title = title,
    description = description,
    author = userName
))

@Composable
private fun ButtonCustomNewTask(
    taskToEdit: Task?,
    enable: Boolean,
    onClick: () -> Unit
) {
    ButtonCustom(
        onClick = onClick,
        text = if (taskToEdit == null) stringResource(create) else stringResource(edit_text),
        primary = true,
        enable = enable
    )
}

@Composable
private fun TextFieldCustomNewTaskName(
    text: String,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = stringResource(name_task),
        placeholder = stringResource(write_name),
        icon = Icons.Rounded.Title,
        contentDescription = stringResource(content_name),
        singleLine = true,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun TextFieldCustomNewTaskDescription(
    text: String,
    keyboardActions: KeyboardActions,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = stringResource(task_description),
        placeholder = stringResource(write_description),
        icon = Icons.Rounded.Description,
        contentDescription = stringResource(content_description),
        keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
            imeAction = ImeAction.Go
        ),
        keyboardActions = keyboardActions,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun ShowTaskNewTask(
    userName: String,
    dateFormatted: String,
    dateCompletedFormatted: String
) {
    ShowTask(
        author = userName,
        date = dateFormatted,
        completedDate = dateCompletedFormatted
    )
}
