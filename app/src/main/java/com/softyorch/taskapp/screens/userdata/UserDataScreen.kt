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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.TaskButton
import com.softyorch.taskapp.utils.textFieldTask
import com.softyorch.taskapp.utils.TopAppBar

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

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = it.calculateTopPadding() * 1.5f,
                                    bottom = it.calculateTopPadding() * 2f
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
                                    singleLine = true
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                val changeData = (
                                        name != userData.username ||
                                                email != userData.userEmail ||
                                                pass != userData.userPass
                                        )

                                TaskButton(
                                    onClick = {
                                        userDataViewModel.updateUserData(
                                            userData = UserData(
                                                id = userData.id,
                                                username = name,
                                                userEmail = email,
                                                userPass = pass,
                                                userPicture = null
                                            )
                                        ).let {
                                            navController.popBackStack()
                                            navController.navigate(AppScreensRoutes.UserDataScreen.route + "/$id")
                                        }
                                    },
                                    text = "Save",
                                    primary = true,
                                    enable = changeData
                                )

                                TaskButton(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                    text = "Cancel",
                                    enable = changeData
                                )
                            }
                        }
                    }
                }
        })
}