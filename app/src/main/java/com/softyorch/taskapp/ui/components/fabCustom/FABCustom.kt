package com.softyorch.taskapp.ui.components.fabCustom

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.ui.widgets.newTask

@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName = viewModel.getUserName()
    val textSizes = viewModel.sizeSelectedOfUser()

    var openDialog by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = {
            openDialog = true
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
        openDialog = newTask(
            addOrEditTaskFunc = viewModel::addTask,
            textSizes = textSizes,
            userName = userName,
            taskToEdit = null
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
