package com.softyorch.taskapp.presentation.widgets.newTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.widgets.RowInfo
import com.softyorch.taskapp.presentation.widgets.ShowTask
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.toStringFormatted
import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1

@Composable
fun newTask(
    addOrEditTaskFunc: KFunction1<Task, Job>,
    userName: String,
    taskToEdit: Task?,
    textSizes: StandardizedSizes
): Boolean {

    val viewModel = hiltViewModel<NewTaskViewModel>()
    val title: String by viewModel.title.observeAsState(initial = taskToEdit?.title ?: "")
    val description: String by viewModel.description.observeAsState(
        initial = taskToEdit?.description ?: ""
    )
    val saveTaskEnable: Boolean by viewModel.saveTaskEnable.observeAsState(initial = taskToEdit != null)

    var openDialog by remember { mutableStateOf(true) }
    val dateCreatedFormatted =
        taskToEdit?.entryDate?.toStringFormatted() ?: Date.from(Instant.now()).toStringFormatted()
    val dateCompletedFormatted =
        taskToEdit?.finishDate?.toStringFormatted() ?: "Unknown"

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
                        RowInfoNewTask(text = "Name of task: ", textSizes = textSizes)
                        TextFieldCustomNewTaskName(text = title, label = "name") {
                            viewModel.onTextFieldChanged(title = it, description = description)
                        }
                        RowInfoNewTask(text = "Task description: ", textSizes = textSizes)
                        TextFieldCustomNewTaskDescription(text = description, label = "description"
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
                                openDialog = false
                            }
                            ButtonCustom(onClick = { openDialog = false }, text = "Cancel")
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
        text = if (taskToEdit == null) "Create" else "Edit Task",
        primary = true,
        enable = enable
    )
}

@Composable
private fun TextFieldCustomNewTaskName(
    text: String,
    label: String,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = label,
        placeholder = "Escribe tu $label",
        icon = Icons.Rounded.Title,
        contentDescription = label,
        singleLine = true,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun TextFieldCustomNewTaskDescription(
    text: String,
    label: String,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = label,
        placeholder = "Escribe una $label",
        icon = Icons.Rounded.Description,
        contentDescription = label,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun RowInfoNewTask(text: String, textSizes: StandardizedSizes) {
    RowInfo(
        text = text,
        paddingStart = 32.dp,
        textSizes = textSizes
    )
}

@Composable
private fun ShowTaskNewTask(userName: String, dateFormatted: String, dateCompletedFormatted: String) {
    ShowTask(
        author = userName,
        date = dateFormatted,
        completedDate = dateCompletedFormatted
    )
}
