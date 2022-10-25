package com.softyorch.taskapp.ui.widgets.newTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.widgets.ShowTask
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.extensions.toStringFormatDate
import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1

@ExperimentalMaterial3Api
@Composable
fun newTask(
    addOrEditTaskFunc: KFunction1<TaskModelUi, Job>,
    userName: String,
    taskToEdit: TaskModelUi?
): Boolean {

    val viewModel = hiltViewModel<NewTaskViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val title: String by viewModel.title.observeAsState(
        initial = taskToEdit?.title ?: emptyString
    )
    val description: String by viewModel.description.observeAsState(
        initial = taskToEdit?.description ?: emptyString
    )
    val saveTaskEnable: Boolean by viewModel.saveTaskEnable.observeAsState(initial = taskToEdit != null)

    var openDialog by remember { mutableStateOf(true) }
    val dateCreatedFormatted =
        taskToEdit?.entryDate?.toStringFormatDate() ?: Date.from(Instant.now())
            .toStringFormatDate()

    val titleDeedCounter: Int by viewModel.titleDeedCounter.observeAsState(initial = 0)

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
                    .height(370.dp)
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
                        TextFieldCustomNewTaskName(
                            text = title,
                            error = errorTittle,
                            titleDeedCounter = titleDeedCounter,
                            limitCharTittle = viewModel.limitCharTittle
                        ) {
                            viewModel.onTextFieldInputChanged(title = it, description = description)
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
                                taskToEdit = taskToEdit,
                                enable = saveTaskEnable,
                                error = error
                            ) {
                                viewModel.onTextInputSend(
                                    title = title,
                                    description = description
                                ).also { error ->
                                    if (error) {
                                        showSnackBarErrors = true
                                    } else {
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
    taskToEdit: TaskModelUi?,
    title: String,
    description: String,
    userName: String
): TaskModelUi = (taskToEdit?.copy(
    title = title,
    description = description
) ?: TaskModelUi(
    title = title,
    description = description,
    author = userName
))

@Composable
private fun ButtonCustomNewTask(
    taskToEdit: TaskModelUi?,
    enable: Boolean,
    error: Boolean = false,
    onClick: () -> Unit
) {
    ButtonCustom(
        text = if (taskToEdit == null) stringResource(create) else stringResource(edit_text),
        enable = enable,
        primary = true,
        error = error,
        onClick = onClick
    )
}

@ExperimentalMaterial3Api
@Composable
fun TextFieldCustomNewTaskName(
    text: String,
    error: Boolean,
    titleDeedCounter: Int,
    limitCharTittle: Int,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask(
            text = text,
            label = stringResource(name_task),
            placeholder = stringResource(write_name),
            singleLine = true,
            newTask = true,
            isError = error,
            onTextFieldChanged = onCheckedChange
        )
        if (titleDeedCounter > 15) Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Chars $titleDeedCounter/$limitCharTittle",
                style = MaterialTheme.typography.labelSmall,
                color =
                if (titleDeedCounter < limitCharTittle) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
        }
        if (error) IconError(errorText = stringResource(text_input_min_little))
    }
}

@ExperimentalMaterial3Api
@Composable
fun TextFieldCustomNewTaskDescription(
    text: String,
    keyboardActions: KeyboardActions,
    error: Boolean,
    onCheckedChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        textFieldCustomNewTask(
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
fun ShowTaskNewTask(
    userName: String,
    dateFormatted: String
) {
    Column {
        ShowTask(
            author = userName,
            date = dateFormatted
        )
        Divider(modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp))
    }

}
