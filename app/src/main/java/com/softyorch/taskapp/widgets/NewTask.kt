package com.softyorch.taskapp.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.components.ButtonCustom
import com.softyorch.taskapp.components.textFieldCustom
import com.softyorch.taskapp.model.Task
import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*
import kotlin.reflect.KFunction1

@Composable
fun newTask(addTask: KFunction1<Task, Job>, userName: String): Boolean {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var openDialog by remember { mutableStateOf(true) }
    val date by remember { mutableStateOf(Date.from(Instant.now()).toString().split(" GMT")[0]) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ShowTask(
            author = userName,
            date = date,
        )

        RowInfo(text = "Name of task: ", paddingStart = 32.dp, fontSize = 16.sp)

        title = textFieldCustom(
            text = title,
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            singleLine = true,
            newTask = true,
        )

        RowInfo(text = "Task description: ", paddingStart = 32.dp, fontSize = 16.sp)

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
                    val task = Task(
                        title = title,
                        description = description,
                        author = userName
                    )
                    addTask(task)
                    openDialog = false
                    //navController.popBackStack()
                },
                text = "Create",
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
    return openDialog
}
