package com.softyorch.taskapp.presentation.widgets.newTask

import android.util.Log
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.presentation.widgets.RowInfo
import com.softyorch.taskapp.presentation.widgets.ShowTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
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

    Log.d("VALUES", "Tittle init -> $title")
    Log.d("VALUES", "Tittle description -> $description")

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
                            dateCompletedFormatted = dateCompletedFormatted, textSizes = textSizes
                        )
                        RowInfoNewTask(text = "Name of task: ", textSizes = textSizes.normalSize)
                        TextFieldCustomNewTaskName(text = title, textSizes = textSizes.normalSize) {
                            viewModel.onTextFieldChanged(title = it, description = description)
                        }
                        RowInfoNewTask(text = "Task description: ", textSizes = textSizes.normalSize)
                        TextFieldCustomNewTaskDescription(
                            text = description,
                            textSizes = textSizes.normalSize,
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    /**Revisar esto, duplica el código*/
                                    addOrEditTaskFunc(
                                        taskToEditOrNewTask(
                                            taskToEdit = taskToEdit, title = title,
                                            description = description, userName = userName
                                        )
                                    )
                                    viewModel.onResetValues()
                                    openDialog = false
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
    textSizes: TextUnit,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = "name",
        placeholder = "Escribe tu name",
        textSizes = textSizes,
        icon = Icons.Rounded.Title,
        contentDescription = "name",
        singleLine = true,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun TextFieldCustomNewTaskDescription(
    text: String,
    textSizes: TextUnit,
    keyboardActions: KeyboardActions,
    onCheckedChange: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = "description",
        placeholder = "Escribe una description",
        textSizes = textSizes,
        icon = Icons.Rounded.Description,
        contentDescription = "description",
        keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
            imeAction = ImeAction.Go
        ),
        keyboardActions = keyboardActions,
        newTask = true,
        onTextFieldChanged = onCheckedChange
    )
}

@Composable
private fun RowInfoNewTask(text: String, textSizes: TextUnit) {
    RowInfo(
        text = text,
        paddingStart = 32.dp,
        textSizes = textSizes
    )
}

@Composable
private fun ShowTaskNewTask(
    userName: String,
    dateFormatted: String,
    dateCompletedFormatted: String,
    textSizes: StandardizedSizes
) {
    ShowTask(
        author = userName,
        date = dateFormatted,
        completedDate = dateCompletedFormatted,
        textSizes = textSizes
    )
}
