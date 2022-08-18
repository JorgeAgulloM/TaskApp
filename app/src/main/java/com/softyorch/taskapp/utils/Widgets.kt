package com.softyorch.taskapp.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.components.ButtonCustom
import com.softyorch.taskapp.components.TextCustom
import com.softyorch.taskapp.components.textFieldCustom
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.screens.main.TaskViewModel
import java.time.Instant
import java.util.*

val elevationDp: Dp = 4.dp
const val elevationF: Float = 4f
val TaskKeyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = true,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Default
)

@Composable
fun newTask(taskViewModel: TaskViewModel): Boolean {

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
            author = "Jorge Agulló",
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
                        author = "Jorge Agulló"
                    )
                    taskViewModel.addTask(task)
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

@Composable
fun RowInfo(
    text: String,
    fontSize: TextUnit = 20.sp,
    paddingStart: Dp = 0.dp,
    heightSize: Dp = 30.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightSize)
            .padding(start = paddingStart, top = 0.dp, bottom = 0.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            style = TextStyle(
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.primary,
                    offset = Offset(
                        x = elevationF,
                        y = elevationF
                    ),
                    blurRadius = elevationF
                )
            )
        )

    }
}

@Composable
fun ShowTask(
    author: String,
    date: String,
    completedDate: String = "",
    paddingStart: Dp = 24.dp
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = paddingStart),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom("Created By:", true)
            TextCustom("Created date:", true)
            if (completedDate.isNotEmpty() && completedDate != "null")
                TextCustom("Completed date:", true)
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            TextCustom(author)
            TextCustom(date)
            if (completedDate.isNotEmpty() && completedDate != "null")
                TextCustom(completedDate)

        }
    }
}



