package com.softyorch.taskapp.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.softyorch.taskapp.components.ButtonCustom
import com.softyorch.taskapp.components.textFieldCustom
import com.softyorch.taskapp.model.Task
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

    var title by remember { mutableStateOf(value = taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(value = taskToEdit?.description ?: "") }
    var openDialog by remember { mutableStateOf(true) }
    val date by remember { mutableStateOf(Date.from(Instant.now())) }
    val dateFormatted = date.toStringFormatted(date = date)

    Dialog(
        onDismissRequest = {
            openDialog = false
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(1.1f)
                    .fillMaxHeight(0.7f)
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

                        ShowTask(
                            author = userName,
                            date = dateFormatted,
                        )

                        RowInfo(
                            text = "Name of task: ",
                            paddingStart = 32.dp,
                            textSizes = textSizes
                        )

                        title = textFieldCustom(
                            text = title,
                            label = "Nombre",
                            placeholder = "Escribe tu nombre",
                            icon = Icons.Rounded.TextFields,
                            contentDescription = "name",
                            singleLine = true,
                            newTask = true,
                        )

                        RowInfo(
                            text = "Task description: ",
                            paddingStart = 32.dp,
                            textSizes = textSizes
                        )

                        description = textFieldCustom(
                            text = description,
                            label = "descripción",
                            placeholder = "Escribe algo...",
                            icon = Icons.Rounded.TextFields,
                            contentDescription = "description",
                            newTask = true,
                        )

                        Column(
                            modifier = Modifier.width(width = 300.dp).padding(top = 16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            ButtonCustom(
                                onClick = {
                                    val task = taskToEdit?.copy(
                                        title = title,
                                        description = description,
                                        entryDate = Date.from(Instant.now())
                                    )
                                        ?: Task(
                                            title = title,
                                            description = description,
                                            author = userName
                                        )
                                    addOrEditTaskFunc(task)
                                    openDialog = false
                                    //navController.popBackStack()
                                },
                                text = if (taskToEdit == null) "Create" else "Edit Task",
                                true
                            )

                            ButtonCustom(
                                onClick = {
                                    title.isBlank()
                                    description.isBlank()
                                    openDialog = false
                                    //navController.popBackStack()
                                },
                                text = "Cancel"
                            )
                        }
                    }
                }
            )
        }
    )
    return openDialog
}
