package com.softyorch.taskapp.screens.userdata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SupervisedUserCircle
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
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.TaskButton
import com.softyorch.taskapp.utils.TaskKeyboardOptions
import com.softyorch.taskapp.utils.textFieldTask
import com.softyorch.taskapp.utils.TopAppBar
import kotlinx.coroutines.Job

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataScreen(
    navController: NavHostController,
    userDataViewModel: UserDataViewModel,
    id: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "User Data",
                icon = Icons.Rounded.SupervisedUserCircle,
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
                                    .padding(top = 32.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End
                            ) {
                                name = textFieldTask(
                                    text = name,
                                    //onTextChange = {},
                                    label = "Name",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    singleLine = true
                                )

                                email = textFieldTask(
                                    text = email,
                                    //onTextChange = {},
                                    label = "Email",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    singleLine = true
                                )

                                pass = textFieldTask(
                                    text = pass,
                                    //onTextChange = {},
                                    label = "Password",
                                    placeholder = "Write your name",
                                    icon = Icons.Rounded.Person,
                                    contentDescription = "Name of user",
                                    keyboardOptions = TaskKeyboardOptions.copy(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    password = true
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                val changeData = (
                                        name != userData.username ||
                                                email != userData.userEmail ||
                                                pass != userData.userPass
                                        )

                                TaskButton(
                                    onClick = {
                                        confirmDialog = true
                                    },
                                    text = "Save",
                                    primary = true,
                                    enable = changeData
                                )

                                TaskButton(
                                    onClick = {
                                        cancelDialog = true
                                    },
                                    text = "Cancel",
                                    enable = changeData
                                )

                                if (confirmDialog) {
                                    confirmDialog = CustomDialog(
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

                                if (cancelDialog) {
                                    cancelDialog = CustomDialog(
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
                            }
                        }
                    }
                }
        })
}

@Composable
private fun CustomDialog(
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
            "Are you sure you want to change the user information?"
        }
        1 -> {
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
                TaskButton(
                    onClick = {
                        openDialog = false
                        when (cancelOrChangeData) {
                            0 -> {
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
                            1 -> {
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
                TaskButton(
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