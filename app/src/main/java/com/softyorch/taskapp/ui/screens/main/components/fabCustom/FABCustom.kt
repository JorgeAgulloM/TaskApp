/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.fabCustom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.SpacerCustom
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.errors.ErrorsNewTaskModel
import com.softyorch.taskapp.ui.models.NewTaskModel
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.components.TextFieldCustomNewTaskDescription
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.components.TextFieldCustomNewTaskName
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.components.ShowTaskNewTask
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.extensions.toStringFormatDate
import java.time.Instant
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun FABCustom(
    hide: Boolean,
    show: (Boolean) -> Unit
) {
    val viewModel = hiltViewModel<FABCustomViewModel>()

    var openDialog by remember { mutableStateOf(false) }
    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 10) * 9
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val calculateWidth = maxWidth - 32
    val focusManager = LocalFocusManager.current

    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    val height by animateIntAsState(
        targetValue = if (openDialog) calculateHeight else 55,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    val width by animateIntAsState(
        targetValue = if (openDialog) calculateWidth else 156,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            if (openDialog) focusManager.moveFocus(FocusDirection.Up)
        }
    )

    if (!openDialog) focusManager.clearFocus()

    AnimatedVisibility(
        visible = !hide,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Box(
            modifier = Modifier
                .height(height.dp)
                .width(width.dp)
                .shadow(
                    ELEVATION_DP,
                    shape = MaterialTheme.shapes.large,
                    ambientColor = MaterialTheme.colorScheme.primary,
                    spotColor = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.BottomEnd
        ) {

            val userName: String by viewModel.userName.observeAsState(initial = "")
            val newTask: NewTaskModel by viewModel.newTask.observeAsState(initial = NewTaskModel())
            val errorsNewTask: ErrorsNewTaskModel by viewModel.errorsNewTask
                .observeAsState(initial = ErrorsNewTaskModel())
            val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
            val titleDeepCounter: Int by viewModel.titleDeedCounter.observeAsState(initial = 0)

            val date = Date.from(Instant.now())
            val dateFormatted = date.toStringFormatDate()

            Column(
                modifier = Modifier
                    .height(height.dp)
                    .width(width.dp)
                    .background(brush = sheetBrush, shape = MaterialTheme.shapes.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                //Head
                ShowTaskNewTask(userName = userName, dateFormatted = dateFormatted)
                //Body
                openDialog = body(
                    newTask,
                    errorsNewTask,
                    titleDeepCounter,
                    viewModel,
                    userName,
                    date,
                    openDialog
                ) { show(it) }

                SpacerCustom()
                //Footer
                Footer(
                    errorsNewTask, isLoading, viewModel, newTask, userName, date
                ) {
                    show(it)
                    openDialog = false
                }
            }

            AnimatedVisibility(
                visible = !openDialog,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(add_task),
                            color = if (openDialog) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Rounded.Add,
                            contentDescription = stringResource(add_task),
                            tint = if (openDialog) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    shape = MaterialTheme.shapes.large,
                    containerColor = if (openDialog) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primaryContainer,
                    onClick = {
                        if (!openDialog) {
                            openDialog = true
                            show(false)
                        }
                    }
                )
            }
        }
    }

}

@Composable
private fun Footer(
    errorsNewTask: ErrorsNewTaskModel,
    isLoading: Boolean,
    viewModel: FABCustomViewModel,
    newTask: NewTaskModel,
    userName: String,
    date: Date,
    show: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ButtonCustom(
            text = stringResource(save),
            primary = true,
            enable = errorsNewTask.isActivatedButton && !isLoading,
            error = errorsNewTask.error
        ) {
            viewModel.onDataSend(
                newTask.copy(
                    title = newTask.title.trim(),
                    description = newTask.description.trim(),
                    author = userName,
                    entryDate = date
                )
            ).let { error ->
                if (!error) {
                    show(true)
                }
            }
        }
        ButtonCustom(text = stringResource(cancel), enable = !isLoading) {
            show(true)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun body(
    newTask: NewTaskModel,
    errorsNewTask: ErrorsNewTaskModel,
    titleDeepCounter: Int,
    viewModel: FABCustomViewModel,
    userName: String,
    date: Date,
    openDialog: Boolean,
    show: (Boolean) -> Unit
): Boolean {
    var open by remember { mutableStateOf(value = false) }
    open = openDialog

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextFieldCustomNewTaskName( //añadir focus a este field
            text = newTask.title,
            error = errorsNewTask.title,
            titleDeedCounter = titleDeepCounter,
            limitCharTittle = viewModel.limitCharTittle
        ) {
            viewModel.onInputChanged(
                newTask.copy(
                    title = it.replaceFirstChar { char ->
                        char.uppercase()
                    }
                )
            )
        }
        TextFieldCustomNewTaskDescription(
            text = newTask.description,
            keyboardActions = KeyboardActions(onGo = {
                viewModel.onDataSend(
                    newTask.copy(
                        title = newTask.title.trim(),
                        description = newTask.description.trim(),
                        author = userName,
                        entryDate = date
                    )
                ).let { error ->
                    if (!error) {
                        open = false
                        show(true)
                    }
                }
            }),
            error = errorsNewTask.description
        ) {
            viewModel.onInputChanged(
                newTask.copy(
                    description = it.replaceFirstChar { char ->
                        char.uppercase()
                    }
                )
            )
        }

        SelectDate()

        SpacerCustom(4.dp)

        var priority by remember { mutableStateOf(value = 0) }
        Priority(priority) { priority = it }

    }
    return open
}

@Composable
private fun Priority(selected: Int, onSelected: (Int) -> Unit) {
    Text(
        text = "Prioridad:",
        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Selectable(Color.White, 0, selected, "None") { onSelected(it) }
        Selectable(Color.Blue, 1, selected, "Low") { onSelected(it) }
        Selectable(Color.Yellow, 2, selected, "Mid") { onSelected(it) }
        Selectable(Color.Red, 3, selected, "High") { onSelected(it) }
    }
}

@Composable
private fun Selectable(
    color: Color,
    priority: Int,
    selected: Int,
    text: String,
    onSelected: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RadioButton(
            selected = priority == selected,
            onClick = { onSelected(priority) },
            colors = RadioButtonDefaults.colors(
                selectedColor = color,
                unselectedColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        Text(
            text = text,
            modifier = Modifier.offset((-10).dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
private fun SelectDate() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    calendar.time = Date()

    var date by remember { mutableStateOf(value = emptyString) }
    var time by remember { mutableStateOf(value = emptyString) }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            time = "$mHour:$mMinute"
        }, hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        context, { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
            date = "$mDay/$mMonth/$mYear"
        }, year, month, day
    )

    Text(
        text = "Fecha de finalización:",
        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )
    Text(
        text = if (date == emptyString || time == emptyString) "Selecciona fecha y hora"
        else "Fecha seleccionada: $date a las $time",
        modifier = Modifier
            .padding(start = 16.dp, bottom = 8.dp)
            .clickable {
                /** estoy hay que revisarlo*/
                timePickerDialog.show()
                datePickerDialog.show()
            },
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    )
}
