package com.softyorch.taskapp.presentation.screens.userdata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.TaskKeyboardOptions
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    viewModel: UserDataViewModel,
    id: String?
) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "User Data",
                nameScreen = AppScreens.UserDataScreen.name,
                navController = navController,
            )
        },
        content = {
            ContentUserDataScreen(it = it, navController = navController, viewModel = viewModel)
        })
}

fun ContentUserDataScreen(
    it: PaddingValues,
    navController: NavHostController,
    viewModel: UserDataViewModel
) {

    val name: String by viewModel.name.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val pass: String by viewModel.pass.observeAsState(initial = "")
    val image: String by viewModel.image.observeAsState(initial = "")
    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)

    var confirmDialog by rememberSaveable { mutableStateOf(false) }
    var cancelDialog by rememberSaveable { mutableStateOf(false) }
    var logOutDialog by rememberSaveable { mutableStateOf(false) }
    IconDataScreen()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = it.calculateTopPadding() * 1.5f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        TextFieldCustomDataScreen(
            text = name, label = "Name", icon = Icons.Rounded.Person
        ) {
            viewModel.viewModelScope.launch {
                viewModel.onDataChange(name = it, email = email, pass = pass, image = image)
            }
        }
        TextFieldCustomDataScreen(
            text = name, label = "Email", icon = Icons.Rounded.Person
        ) {
            viewModel.viewModelScope.launch {
                viewModel.onDataChange(name = name, email = it, pass = pass, image = image)
            }
        }
        TextFieldCustomDataScreen(
            text = name, label = "Name", icon = Icons.Rounded.Person
        ) {
            viewModel.viewModelScope.launch {
                viewModel.onDataChange(name = name, email = email, pass = it, image = image)
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonCustomDataScreen(text = "Logout", primary = true) { cancelDialog = true } //viewModel
        Spacer(modifier = Modifier.padding(bottom = 4.dp))
        ButtonCustomDataScreen(text = "Save", enable = saveEnabled, primary = true) {
            confirmDialog = true
        }
        ButtonCustomDataScreen(text = "Cancel", enable = saveEnabled) {
            cancelDialog = true
        }
    }


    if (logOutDialog) {
        logOutDialog = UserDataDialog(
            cancelOrChangeData = 0,
            userDataViewModel = viewModel,
            userData = userData,
            name = name,
            email = email,
            pass = pass,
            navController = navController,
            id = id
        )
    }

    if (confirmDialog) {
        confirmDialog = UserDataDialog(
            cancelOrChangeData = 1,
            userDataViewModel = viewModel,
            userData = userData,
            name = name,
            email = email,
            pass = pass,
            navController = navController,
            id = id
        )
    }

    if (cancelDialog) {
        cancelDialog = UserDataDialog(
            cancelOrChangeData = 2,
            userDataViewModel = viewModel,
            userData = userData,
            name = name,
            email = email,
            pass = pass,
            navController = navController,
            id = id
        )
    }
}

@Composable
private fun ButtonCustomDataScreen(
    text: String, enable: Boolean = true, primary: Boolean = false, onclick: () -> Unit
) {
    ButtonCustom(
        onClick = { onclick() },
        text = text,
        enable = enable,
        primary = primary
    )
}

@Composable
private fun TextFieldCustomDataScreen(
    text: String,
    label: String,
    icon: ImageVector,
    onTextFieldChanged: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = label,
        placeholder = "Write your ${label.lowercase()}",
        icon = icon,
        contentDescription = "$label of user",
        singleLine = true,
        onTextFieldChanged = { onTextFieldChanged() }
    )
}

@Composable
private fun IconDataScreen() {
    Icon(
        imageVector = Icons.Rounded.Person,
        contentDescription = "Image of user",
        modifier = Modifier
            .size(194.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium.copy(
                    CornerSize(25.dp)
                )
            )
    )
}




@Composable
private fun UserDataDialog(
    cancelOrChangeData: Int = 0,
    userDataViewModel: UserDataViewModel,
    userData: UserData,
    name: String,
    email: String,
    pass: String,
    rememberMe: Boolean = false,
    navController: NavHostController,
    id: String?,
    onConfirmButtonClick: () -> Unit
): Boolean {
    var openDialog by rememberSaveable { mutableStateOf(value = true) }
    val text = when (cancelOrChangeData) {
        0 -> {
            "Are you sure you want to log out?"
        }
        1 -> {
            "Are you sure you are not making any modifications?"
        }
        2 -> {
            "Are you sure you are not making any modifications?"
        }
        else -> {
            "Error"
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                ButtonCustom(
                    onClick = {
                        openDialog = false
                        when (cancelOrChangeData) {
                            0 -> {
                                userDataViewModel.logOut().let {
                                    navController.popBackStack()
                                    navController.navigate(AppScreensRoutes.LoginScreen.route)
                                }
                            }
                            1 -> {
                                userDataViewModel.updateUserData(
                                    userData = UserData(
                                        id = userData.id,
                                        username = name,
                                        userEmail = email,
                                        userPass = pass,
                                        userPicture = null,
                                        rememberMe = rememberMe
                                    )
                                ).let {
                                    navController.popBackStack()
                                    navController.navigate(AppScreensRoutes.UserDataScreen.route + "/$id")
                                }
                            }
                            2 -> {
                                navController.popBackStack()
                            }
                            else -> {
                                //TODO
                            }
                        }
                    },
                    text = "Yes, i sure",
                    primary = true
                )
            },
            dismissButton = {
                ButtonCustom(
                    onClick = {
                        openDialog = false
                    },
                    text = "Cancel"
                )
            },
            text = {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            }
        )
    }
    return openDialog
}