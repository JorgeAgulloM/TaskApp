package com.softyorch.taskapp.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Details
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.navigation.TaskAppScreens
import com.softyorch.taskapp.screens.main.TaskViewModel
import com.softyorch.taskapp.utils.RowIndication
import com.softyorch.taskapp.utils.TaskSummary
import com.softyorch.taskapp.utils.TopAppBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = hiltViewModel(),
    id: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Details",
                icon = Icons.Rounded.Details,
                nameScreen = TaskAppScreens.DetailsScreen.name,
                navController = navController,
            )
        }
    ) {
        Content(it = it, taskViewModel = taskViewModel, navController = navController, id = id)
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavController,
    id: String
) {
    produceState<Resource<Task>>(initialValue = Resource.Loading()) {
        value = taskViewModel.getTaskId(id = id)
    }.value
        .let { data ->
            data.data?.let { task ->
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(
                            top = it.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                ) {
                    //RowIndication(task.title, fontSize = 16.sp)
                    TaskSummary(task.checkState, onCheckedChange = {}, task.title, onclick = {})
                    RowIndication(task.description, fontSize = 16.sp, heightSize = 300.dp)
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    RowIndication("Details", paddingStart = 16.dp)
                    /*RowIndication(text = "Created By:", task.author, fontSize = 16.sp)
                    RowIndication(
                        text = "Created Date:",
                        task.entryDate.toString(),
                        fontSize = 16.sp
                    )*/


                }
            }
        }
}