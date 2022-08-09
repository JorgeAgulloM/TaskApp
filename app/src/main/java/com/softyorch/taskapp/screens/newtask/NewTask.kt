package com.softyorch.taskapp.screens.newtask

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.utils.*
import java.time.Instant
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(navController: NavController, taskViewModel: TaskViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = "New Task",
            icon = Icons.Rounded.Settings,
            nameScreen = TaskAppScreens.SettingsScreen.name,
            navController = navController,
        )
    },
        content = {
            Content(
                it = it,
                navController = navController,
                taskViewModel = taskViewModel
            )
        })
}


@Composable
fun Content(it: PaddingValues, navController: NavController, taskViewModel: TaskViewModel) {

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(top = it.calculateTopPadding() * 1.5f, start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InfoTask(
            author = "Jorge Agulló",
            date = Date.from(Instant.now()).toString(),
            //completedDate = Date.from(Instant.now()).toString()
        )

        RowIndication(text = "Name of task: ", paddingStart = 32.dp, fontSize = 16.sp)
        TextFieldTask(
            text = title,
            label = "Nombre",
            onTextChange = {
                title = it
            },
            placeholder = "Escribe tu nombre",
            icon = Icons.Rounded.TextFields,
            contentDescription = "name",
            singleLine = true,
            newTask = true,
        )
        RowIndication(text = "Task description: ", paddingStart = 32.dp, fontSize = 16.sp)
        TextFieldTask(
            text = description,
            label = "descripción",
            onTextChange = {
                description = it
            },
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
                onclick = {
                    val task = Task(
                        title = title,
                        description = description,
                        author = "Jorge Agulló",
                        checkState = false
                    )
                    taskViewModel.addTask(task)
                    navController.popBackStack()
                },
                text = "Create",
                true
            )
            TaskButton(
                onclick = {
                    title.isBlank()
                    description.isBlank()
                    navController.popBackStack()
                },
                text = "Cancel"
            )
        }
    }
}



