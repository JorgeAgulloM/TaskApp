package com.softyorch.taskapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.ui.theme.DarkMode90t
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
fun TopAppBar(
    title: String,
    icon: ImageVector? = null,
    isMainScreen: Boolean = false,
    nameScreen: String,
    navController: NavController,
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {
    SmallTopAppBar(
        modifier = Modifier.shadow(
            elevation = 4.dp, shape = MaterialTheme.shapes.large.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ),
        title = {
            Text(
                text = title,
                color = LightMode90t,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.secondary),
        navigationIcon = {
            if (!isMainScreen) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Go Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            if (nameScreen != AppScreens.MainScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.MainScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Go Home",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.HistoryScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.HistoryScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.SettingsScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.SettingsScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.UserDataScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.UserDataScreen.route + "/${"0"}")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.SupervisedUserCircle,
                        contentDescription = "User data",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@Composable
fun FAB(navController: NavController, taskViewModel: TaskViewModel) {

    var openDialog by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = {
            openDialog = true
            //navController.navigate(TaskAppScreens.NewTaskScreen.name)
        },
        modifier = Modifier.size(50.dp),
        contentColor = MaterialTheme.colorScheme.secondary,
        containerColor = MaterialTheme.colorScheme.tertiary,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add task"
            )
        }
    )

    if (openDialog) {
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
                        openDialog = newTask(taskViewModel = taskViewModel)
                    }
                )
            }
        )
    }

    //Material you version
    /*ExtendedFloatingActionButton(
        onClick = {
            //TODO, Crear contenido modal o flotante.
            navController.navigate(TaskAppScreens.NewTaskScreen.name)
        },
        //modifier = Modifier.size(50.dp),
        contentColor = MaterialTheme.colorScheme.secondary,
        containerColor = MaterialTheme.colorScheme.tertiary,
        shape = MaterialTheme.shapes.medium,
        icon = {
            Icon(
                //modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add task"
            )
        },
        text = { Text(text = "Add")}
    )*/
}

@Composable
private fun newTask(taskViewModel: TaskViewModel): Boolean {

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

        InfoTask(
            author = "Jorge Agulló",
            date = date,
        )

        RowIndication(text = "Name of task: ", paddingStart = 32.dp, fontSize = 16.sp)

        title = textFieldTask(
            text = title,
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            singleLine = true,
            newTask = true,
        )

        RowIndication(text = "Task description: ", paddingStart = 32.dp, fontSize = 16.sp)

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

            TaskButton(
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

            TaskButton(
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
fun TaskButton(
    onClick: () -> Unit,
    text: String,
    primary: Boolean = false,
    enable: Boolean = true

) {

    //TODO

    Button(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier.width(114.dp).height(26.dp).padding(2.dp),
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (primary) MaterialTheme.colorScheme.tertiary else Color.Transparent,
            contentColor = if (primary) DarkMode90t else MaterialTheme.colorScheme.onSurface
        ),
        content = {
            Text(
                text = text,
                style = TextStyle(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.primary,
                        offset = if (primary) Offset(x = 0f, y = 0f) else Offset(
                            x = elevationF,
                            y = elevationF
                        ),
                        blurRadius = if (primary) 0f else elevationF
                    )
                ),
                textDecoration = if (!primary) TextDecoration.Underline else null
            )
        },
        contentPadding = PaddingValues(2.dp),
        elevation = ButtonDefaults.buttonElevation(if (primary) elevationDp else 0.dp)
    )
}

@Composable
fun RowIndication(
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
fun InfoTask(
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
            InfoText("Created By:", true)
            InfoText("Created date:", true)
            if (completedDate.isNotEmpty() && completedDate != "null")
                InfoText("Completed date:", true)
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            InfoText(author)
            InfoText(date)
            if (completedDate.isNotEmpty() && completedDate != "null")
                InfoText(completedDate)
        }
    }
}

@Composable
private fun InfoText(
    text: String,
    description: Boolean = false
) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 14.sp,
        fontWeight = if (description) FontWeight.SemiBold else FontWeight.Normal
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSummaryCheck(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    onClick: () -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {

    val onChange by rememberSaveable { mutableStateOf(checked) }

    Row(
        modifier = Modifier.fillMaxWidth(1f).padding(end = 8.dp).height(30.dp).clickable {
            onClick.invoke()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        content = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary,
                    checkmarkColor = MaterialTheme.colorScheme.secondary
                )
            )
            Text(
                text = text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color =
                if (checked)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                style = TextStyle()
            )
        }
    )
}

@Composable
fun TaskSwitch(
    text: String,
    checked: Boolean = false,
    enable: Boolean = true,
    onCheckedChange: () -> Unit
) {

    var stateSwitch by rememberSaveable { mutableStateOf(checked) }

    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Switch(
            checked = stateSwitch,
            onCheckedChange = {
                stateSwitch = !stateSwitch
                onCheckedChange.invoke()
            },
            thumbContent = {
                if (stateSwitch)
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = text,
                    )
            },
            enabled = enable,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedIconColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}