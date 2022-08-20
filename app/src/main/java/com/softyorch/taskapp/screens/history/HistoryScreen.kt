package com.softyorch.taskapp.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.screens.main.MainViewModel


@ExperimentalMaterial3Api
@Composable
fun HistoryScreen(navController: NavHostController, mainViewModel: MainViewModel) {

    val viewMode = hiltViewModel<HistoryViewModel>()

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "History",
                nameScreen = AppScreens.HistoryScreen.name,
                navController = navController,
            )
        },
        content = {
            Content(it = it, navController = navController, viewModel = viewMode)
        })
}

@Composable
private fun Content(
    it: PaddingValues,
    navController: NavHostController,
    viewModel: HistoryViewModel
) {
    val tasks = viewModel.taskList.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 16.dp, end = 16.dp)
    ) {
        items(tasks) { task ->
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable {
                    navController.navigate(AppScreens.DetailsScreen.name + "/${task.id}")
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = task.entryDate.toInstant().toString().split("T")[0],
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = task.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}