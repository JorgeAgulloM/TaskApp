package com.softyorch.taskapp.presentation.components.fabCustom

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.widgets.newTask.newTask

@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")

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
                contentDescription = stringResource(add_task)
            )
        }
    )

    if (openDialog) {
        openDialog = newTask(
            addOrEditTaskFunc = viewModel::addTask,
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
