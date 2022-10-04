package com.softyorch.taskapp.ui.components.fabCustom

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
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.widgets.newTask.newTask

@ExperimentalMaterial3Api
@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")

    var openDialog by remember { mutableStateOf(false) }

    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(add_task)) },
        icon = {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(add_task)
            )
        },
        onClick = { openDialog = true }
    )

    if (openDialog) {
        openDialog = newTask(
            addOrEditTaskEntityFunc = viewModel::addTask,
            userName = userName,
            taskEntityToEdit = null
        )
    }
}
