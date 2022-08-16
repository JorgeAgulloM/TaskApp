package com.softyorch.taskapp.screens.userdata

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SupervisedUserCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.utils.TaskButton
import com.softyorch.taskapp.utils.textFieldTask
import com.softyorch.taskapp.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserdataScreen(navController: NavHostController, userDataViewModel: UserDataViewModel) {
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
                    textFieldTask(
                        "Jorge Agulló",
                        //onTextChange = {},
                        label = "Name",
                        placeholder = "Write your name",
                        icon = Icons.Rounded.Person,
                        contentDescription = "Name of user",
                        singleLine = true
                    )
                    textFieldTask(
                        "agullójorge@gmail.com",
                        //onTextChange = {},
                        label = "Name",
                        placeholder = "Write your name",
                        icon = Icons.Rounded.Person,
                        contentDescription = "Name of user",
                        singleLine = true
                    )
                    textFieldTask(
                        "123456789A",
                        //onTextChange = {},
                        label = "Name",
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
                    TaskButton(
                        onclick = {},
                        text = "Save",
                        primary = true
                    )
                    TaskButton(
                        onclick = {},
                        text = "Cancel"
                    )
                }
            }
        })
}