package com.softyorch.taskapp.utils

import android.webkit.WebSettings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.ui.theme.DarkMode90t
import com.softyorch.taskapp.ui.theme.LightMode90t

val elevationDp: Dp = 4.dp
val elevationF: Float = 4f

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

    //val scaffoldState = rememberScrollState()
    //val scope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            navController.navigate(TaskAppScreens.NewTaskScreen.name)
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

//TextField V1
@Composable
fun TextFieldTask(
    text: String = "",
    onTextChange: (String) -> Unit,
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
    singleLine: Boolean = false,
    newTask: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    var description by rememberSaveable() { mutableStateOf(text) }
    val focusedColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
    val unfocusedColor: Color = LightMode90t.copy(alpha = 0.8f)
    val corner: Dp = 20.dp

    val personalizedShape: Shape = MaterialTheme.shapes.extraLarge.copy(
        topStart = CornerSize(corner),
        bottomStart = CornerSize(corner),
        topEnd = if (newTask) CornerSize(corner) else ZeroCornerSize,
        bottomEnd = if (newTask) CornerSize(corner) else ZeroCornerSize
    )
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier.padding(4.dp).width(width = 370.dp).shadow(
            elevation = elevationDp, shape = personalizedShape
        ),
        readOnly = readOnly,
        textStyle = TextStyle(color = LightMode90t),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = contentDescription) },
        isError = isError,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        interactionSource = mutableInteractionSource,
        shape = personalizedShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = LightMode90t,
            placeholderColor = LightMode90t.copy(alpha = 0.4f),
            focusedLabelColor = unfocusedColor,
            unfocusedLabelColor = unfocusedColor,
            unfocusedLeadingIconColor = unfocusedColor,
            focusedLeadingIconColor = focusedColor,
            containerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = focusedColor
        )
    )
}

@Composable
fun TaskButton(
    onclick: () -> Unit,
    text: String,
    primary: Boolean = false

) {

    //TODO

    Button(
        onClick = onclick,
        modifier = Modifier.width(114.dp).height(26.dp).padding(2.dp),
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
                )
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
    heightSize: Dp = 30.dp
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightSize)
            .padding(start = paddingStart, top = 0.dp, bottom = 0.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
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
    onclick: () -> Unit
) {

    val onChange by rememberSaveable() { mutableStateOf(checked) }

    Row(
        modifier = Modifier.fillMaxWidth(1f).height(30.dp).clickable {
            onclick.invoke()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        content = {
            Checkbox(
                checked = onChange,
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