package com.softyorch.taskapp.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.ui.theme.LightMode90t

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
        modifier = Modifier.shadow(elevation = 4.dp),
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
            if (nameScreen != TaskAppScreens.MainScreen.name) {
                IconButton(onClick = {
                    navController.navigate(TaskAppScreens.MainScreen.name)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Go Home",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != TaskAppScreens.HistoryScreen.name) {
                IconButton(onClick = {
                    navController.navigate(TaskAppScreens.HistoryScreen.name)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != TaskAppScreens.SettingsScreen.name) {
                IconButton(onClick = {
                    navController.navigate(TaskAppScreens.SettingsScreen.name)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != TaskAppScreens.UserDataScreen.name) {
                IconButton(onClick = {
                    navController.navigate(TaskAppScreens.UserDataScreen.name)
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
fun FAB(navController: NavController) {

    val scaffoldState = rememberScrollState()
    val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            //TODO, Crear contenido modal o flotante.
            navController.navigate(TaskAppScreens.NewTaskScreen.name)
        },
        modifier = Modifier.size(50.dp),
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.tertiary,
        shape = CircleShape,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add task"
            )
        }
    )
}

@Composable
fun TextFieldTask(
    text: String = "",
    label: String = "",
    placeholder: String = "",
    icon: ImageVector,
    contentDescription: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = true,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Default
    ),
    multiLine: Boolean = false,
    newTask: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    var description by remember { mutableStateOf(text) }
    val focused: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
    val unfocused: Color = LightMode90t.copy(alpha = 0.8f)
    TextField(
        value = description,
        onValueChange = {
            description = it
        },
        modifier = Modifier.padding(4.dp).width(width = 350.dp),
        readOnly = readOnly,
        textStyle = TextStyle(color = LightMode90t),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = contentDescription) },
        isError = isError,
        keyboardOptions = keyboardOptions,
        singleLine = multiLine,
        interactionSource = mutableInteractionSource,
        shape = MaterialTheme.shapes.extraLarge.copy(
            topStart = CornerSize(25.dp),
            bottomStart = CornerSize(25.dp),
            topEnd = if (newTask) CornerSize(25.dp) else ZeroCornerSize,
            bottomEnd = if (newTask) CornerSize(25.dp) else ZeroCornerSize
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = LightMode90t,
            placeholderColor = LightMode90t.copy(alpha = 0.4f),
            focusedLabelColor = focused,
            unfocusedLabelColor = unfocused,
            unfocusedLeadingIconColor = unfocused,
            focusedLeadingIconColor = focused,
            containerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = focused
        )
    )
}

@Composable
fun PrimaryButton() {

    //TODO

    Button(onClick = {}, modifier = Modifier.padding(4.dp)) {

    }
}