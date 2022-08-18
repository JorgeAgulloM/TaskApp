package com.softyorch.taskapp.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.ui.theme.LightMode90t
import java.time.Instant
import java.util.*

val elevationDp: Dp = 4.dp
val elevationF: Float = 4f
val TaskKeyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
    capitalization = KeyboardCapitalization.Sentences,
    autoCorrect = true,
    keyboardType = KeyboardType.Text,
    imeAction = ImeAction.Default
)

@Composable
fun Hello(name: String = "null") {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$name Screen", textAlign = TextAlign.Center)
        }
    }
}


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

        title = textFieldTask(
            text = title,
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            singleLine = true,
            newTask = true,
        )

        RowInfo(text = "Task description: ", paddingStart = 32.dp, fontSize = 16.sp)

        description = textFieldTask(
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

//TextField V1
@Composable
fun textFieldTask(
    text: String = "",
    label: String = "",
    placeholder: String = "",
    icon: ImageVector,
    contentDescription: String,
    keyboardOptions: KeyboardOptions = TaskKeyboardOptions,
    singleLine: Boolean = false,
    newTask: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false,
    password: Boolean = false
): String {
    val focusedColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
    val unfocusedColor: Color = LightMode90t.copy(alpha = 0.8f)
    val personalizedShape: Shape = MaterialTheme.shapes.large.copy(
        //topStart = CornerSize(corner),
        //bottomStart = CornerSize(corner),
        topEnd = if (newTask) MaterialTheme.shapes.large.topEnd else ZeroCornerSize,
        bottomEnd = if (newTask) MaterialTheme.shapes.large.bottomEnd else ZeroCornerSize
    )
    val textChange = rememberSaveable { mutableStateOf(text) }
    var passVisible by rememberSaveable { mutableStateOf(password) }

    TextField(
        value = textChange.value,
        onValueChange = { textChange.value = it },
        modifier = Modifier
            .padding(
                start = if (newTask) 8.dp else 32.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = if (newTask) 8.dp else 0.dp
            )
            .width(width = if (newTask) 370.dp else 270.dp)
            .shadow(
                elevation = elevationDp, shape = personalizedShape
            ),
        readOnly = readOnly,
        textStyle = TextStyle(color = LightMode90t),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = contentDescription) },
        trailingIcon = {
            if (password) {
                val image = if (passVisible)
                    Icons.Rounded.Visibility
                else
                    Icons.Rounded.VisibilityOff

                val description = if (passVisible)
                    "Hide Password"
                else
                    "Show Password"

                IconButton(
                    onClick = {
                        passVisible = !passVisible
                    },
                    content = {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = LightMode90t
                        )
                    }
                )
            }
        },
        isError = isError,
        visualTransformation = if (!passVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = 5,
        shape = personalizedShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = LightMode90t,
            placeholderColor = LightMode90t.copy(alpha = 0.4f),
            focusedLabelColor = unfocusedColor,
            unfocusedLabelColor = unfocusedColor,
            unfocusedLeadingIconColor = unfocusedColor,
            focusedLeadingIconColor = focusedColor,
            containerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = focusedColor,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = focusedColor
        )
    )

    return textChange.value
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



