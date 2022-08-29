package com.softyorch.taskapp.presentation.screens.userdata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.TaskKeyboardOptions

@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    userDataViewModel: UserDataViewModel,
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

            val data = userDataViewModel.userDataList.collectAsState().value
            val userId = id.let {
                if (id == "0") {
                    data[0].id.toString()
                } else {
                    id
                }
            }

            produceState<Resource<UserData>>(initialValue = Resource.Loading()) {
                value = userDataViewModel.getUserDataId(id = userId.toString())
            }.value
                .let { dataIt ->
                    dataIt.data?.let { userData ->
                        var name by remember { mutableStateOf(userData.username) }
                        var email by remember { mutableStateOf(userData.userEmail) }
                        var pass by remember { mutableStateOf(userData.userPass) }
                        var confirmDialog by rememberSaveable { mutableStateOf(false) }
                        var cancelDialog by rememberSaveable { mutableStateOf(false) }
                        var logOutDialog by rememberSaveable { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = it.calculateTopPadding() * 1.5f
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
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

                            Column(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                name = textFieldCustom(
                                    text = name,
                                    //onTextChange = {},
                                    label = "Name",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    singleLine = true,
                                    //onTextFieldChanged = onTextFieldChanged
                                )

                                email = textFieldCustom(
                                    text = email,
                                    //onTextChange = {},
                                    label = "Email",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    singleLine = true,
                                    //onTextFieldChanged = onTextFieldChanged
                                )

                                pass = textFieldCustom(
                                    text = pass,
                                    //onTextChange = {},
                                    label = "Password",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    keyboardOptions = TaskKeyboardOptions.copy(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    password = true,
                                    //onTextFieldChanged = onTextFieldChanged
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                val changeData = (
                                        name != userData.username ||
                                                email != userData.userEmail ||
                                                pass != userData.userPass
                                        )

                                ButtonCustom(
                                    onClick = {
                                        logOutDialog = true
                                    },
                                    text = "LogOut",
                                    primary = true
                                )

                                ButtonCustom(
                                    onClick = {
                                        confirmDialog = true
                                    },
                                    text = "Save",
                                    primary = true,
                                    enable = changeData
                                )

                                ButtonCustom(
                                    onClick = {
                                        cancelDialog = true
                                    },
                                    text = "Cancel",
                                    enable = changeData
                                )

                                if (logOutDialog) {
                                    logOutDialog = UserDataDialog(
                                        cancelOrChangeData = 0,
                                        userDataViewModel = userDataViewModel,
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
                                        userDataViewModel = userDataViewModel,
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
                                        userDataViewModel = userDataViewModel,
                                        userData = userData,
                                        name = name,
                                        email = email,
                                        pass = pass,
                                        navController = navController,
                                        id = id
                                    )
                                }
                            }
                        }
                    }
                }
        })
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
    id: String?
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