package com.softyorch.taskapp.presentation.components.fabCustom

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.widgets.newTask.newTask

@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")

    var openDialog by remember { mutableStateOf(false) }
/*    FloatingActionButton(
        onClick = {
            openDialog = true
        },
        modifier = Modifier.size(50.dp),
        //contentColor = MaterialTheme.colorScheme.secondary,
        //containerColor = MaterialTheme.colorScheme.tertiary,
        content = {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(add_task)
            )
        }
    )*/


    //Material you version
    ExtendedFloatingActionButton(
        onClick = { openDialog = true },
        //modifier = Modifier.size(50.dp),
        //contentColor = MaterialTheme.colorScheme.secondary,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        //shape = MaterialTheme.shapes.medium,
        icon = {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(add_task)
            )
        },
        text = { Text(text = stringResource(add_task)) }
    )

    if (openDialog) {
        openDialog = newTask(
            addOrEditTaskFunc = viewModel::addTask,
            userName = userName,
            taskToEdit = null
        )
    }
}
