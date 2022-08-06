package com.softyorch.taskapp.screens.newtask

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.utils.RowIndication
import com.softyorch.taskapp.utils.TaskButton
import com.softyorch.taskapp.utils.TextFieldTask
import com.softyorch.taskapp.utils.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = "New Task",
            icon = Icons.Rounded.Settings,
            nameScreen = TaskAppScreens.SettingsScreen.name,
            navController = navController,
        )
    },
        content = {
            Content(it = it)
        })
}

@Composable
fun Content(it: PaddingValues) {
    Column(
        modifier = Modifier.padding(top = it.calculateTopPadding() * 1.5f, start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RowIndication(text = "Created by: ", textEdit = "Jorge Agulló")
        RowIndication(text = "Created data: ", textEdit = "05/08/2022")
        RowIndication(text = "Name of task: ")
        TextFieldTask(
            label = "Nombre",
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            singleLine = true,
            newTask = true,
        )
        RowIndication(text = "Task description: ")
        TextFieldTask(
            label = "descripción",
            placeholder = "Escribe algo...",
            icon = Icons.Rounded.TextFields,
            contentDescription = "description",
            newTask = true,
        )
        Column (
            modifier = Modifier.width(width = 300.dp).padding(top = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TaskButton(text = "Create" , true)
            TaskButton(text = "Cancel")
        }
    }
}

