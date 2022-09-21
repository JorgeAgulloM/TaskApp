package com.softyorch.taskapp.ui.widgets.newTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.toStringFormatDate
import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1

@ExperimentalMaterial3Api
@Composable
fun newTask(
    addOrEditTaskEntityFunc: KFunction1<TaskEntity, Job>,
    userName: String,
    taskEntityToEdit: TaskEntity?
): Boolean {

    val viewModel = hiltViewModel<NewTaskViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val title: String by viewModel.title.observeAsState(initial = taskEntityToEdit?.title ?: emptyString)
    val description: String by viewModel.description.observeAsState(
        initial = taskEntityToEdit?.description ?: emptyString
    )
    val saveTaskEnable: Boolean by viewModel.saveTaskEnable.observeAsState(initial = taskEntityToEdit != null)

    var openDialog by remember { mutableStateOf(true) }
    val dateCreatedFormatted =
        taskEntityToEdit?.entryDate?.toStringFormatDate() ?: Date.from(Instant.now()).toStringFormatDate()

    /** Error states */
    val errorTittle: Boolean by viewModel.errorTittle.observeAsState(initial = false)
    val errorDescription: Boolean by viewModel.errorDescription.observeAsState(initial = false)
    val error: Boolean by viewModel.error.observeAsState(initial = false)

    if (isLoading) CircularIndicatorCustom(stringResource(loading_loading))

    Dialog(
        onDismissRequest = {
            openDialog = false
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.78f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    ),
                content = {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ShowTaskNewTask(
                            userName = userName, dateFormatted = dateCreatedFormatted
                        )
                        TextFieldCustomNewTaskName(text = title, error = errorTittle) {
                            viewModel.onTextFieldInputChanged(title = it, description = description)
                        }
                        TextFieldCustomNewTaskDescription(
                            text = description,
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    /**Revisar esto, duplica el código*/
                                    if (saveTaskEnable) {
                                        addOrEditTaskEntityFunc(
                                            taskToEditOrNewTask(
                                                taskEntityToEdit = taskEntityToEdit, title = title,
                                                description = description, userName = userName
                                            )
                                        )
                                        viewModel.onResetValues()
                                        openDialog = false
                                    }
                                }
                            ),
                            error = errorDescription
                        ) {
                            viewModel.onTextFieldInputChanged(title = title, description = it)
                        }

                        var showSnackBarErrors by remember { mutableStateOf(value = false) }
                        Column(
                            modifier = Modifier.width(width = 300.dp).padding(top = 16.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ButtonCustomNewTask(
                                taskEntityToEdit = taskEntityToEdit, enable = saveTaskEnable, error = error
                            ) {
                                viewModel.onTextInputSend(
                                    title = title,
                                    description = description
                                ).also { error ->
                                        if (error) {
                                            showSnackBarErrors = true
                                        } else {
                                            addOrEditTaskEntityFunc(
                                                taskToEditOrNewTask(
                                                    taskEntityToEdit = taskEntityToEdit, title = title,
                                                    description = description, userName = userName
                                                )
                                            )
                                            viewModel.onResetValues()
                                            openDialog = false
                                        }
                                    }
                            }
                            ButtonCustom(
                                onClick = {
                                    openDialog = false
                                    viewModel.onResetValues()
                                }, text = stringResource(
                                    cancel
                                )
                            )
                        }

                        if (!error) showSnackBarErrors = false
                        if (showSnackBarErrors) SnackBarError {
                            showSnackBarErrors = false
                        }
                    }
                }
            )
        }
    )
    return openDialog
}

private fun taskToEditOrNewTask(
    taskEntityToEdit: TaskEntity?,
    title: String,
    description: String,
    userName: String
): TaskEntity = (taskEntityToEdit?.copy(
    title = title,
    description = description
) ?: TaskEntity(
    title = title,
    description = description,
    author = userName
))

@Composable
private fun ButtonCustomNewTask(
    taskEntityToEdit: TaskEntity?,
    enable: Boolean,
    error: Boolean = false,
    onClick: () -> Unit
) {
    ButtonCustom(
        text = if (taskEntityToEdit == null) stringResource(create) else stringResource(edit_text),
        enable = enable,
        primary = true,
        error = error,
        onClick = onClick
    )
}

@ExperimentalMaterial3Api
@Composable
private fun TextFieldCustomNewTaskName(
    text: String,
    error: Boolean,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask (
            text = text,
            label = stringResource(name_task),
            placeholder = stringResource(write_name),
            singleLine = true,
            newTask = true,
            isError = error,
            onTextFieldChanged = onCheckedChange
        )
        if (error) IconError(errorText = stringResource(text_input_min_little))
    }
}

@ExperimentalMaterial3Api
@Composable
private fun TextFieldCustomNewTaskDescription(
    text: String,
    keyboardActions: KeyboardActions,
    error: Boolean,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask (
            text = text,
            label = stringResource(task_description),
            placeholder = stringResource(write_description),
            keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
                imeAction = ImeAction.Go
            ),
            keyboardActions = keyboardActions,
            newTask = true,
            isError = error,
            onTextFieldChanged = onCheckedChange
        )
        if (error) IconError(errorText = stringResource(text_input_min_little))
    }
}

@Composable
private fun ShowTaskNewTask(
    userName: String,
    dateFormatted: String
) {
    Column {
        ShowTask(
            author = userName,
            date = dateFormatted
        )
        Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
    }

}
