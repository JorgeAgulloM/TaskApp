package com.softyorch.taskapp.screens.history

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.utils.Hello
import com.softyorch.taskapp.utils.RowIndication
import com.softyorch.taskapp.utils.TopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    Hello("History")
    Scaffold(
        topBar = {
            TopAppBar(
                title = "History",
                icon = Icons.Rounded.History,
                nameScreen = TaskAppScreens.HistoryScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, navController = navController, taskViewModel = taskViewModel)
        })
}

@Composable
private fun Content(
    it: PaddingValues,
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {
    val tasks = taskViewModel.taskList.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding(), start = 8.dp, end = 8.dp)
    ) {
        items(tasks) { task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = task.entryDate.toString())
                Text(text = task.title)
            }
        }
    }
}