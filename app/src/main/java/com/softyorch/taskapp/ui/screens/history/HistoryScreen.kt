package com.softyorch.taskapp.ui.screens.history

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.ui.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.R.string.history
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.utils.*

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun HistoryScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HistoryViewModel>()

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(history),
                nameScreen = AppScreens.HistoryScreen.name,
                navController = navController,
            )
        }
    ) {
        Content(it = it, navController = navController, viewModel = viewModel)
    }
}

@Composable
private fun Content(
    it: PaddingValues,
    navController: NavHostController,
    viewModel: HistoryViewModel
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)

    val taskEntities: List<TaskEntity> by viewModel.taskEntityList.observeAsState(initial = emptyList())

    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(top = it.calculateTopPadding() * 1.5f, start = 16.dp, end = 16.dp)
    ) {
        items(taskEntities) { task ->
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable {
                        navController.navigate(AppScreens.DetailsScreen.name + "/${task.id}")
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                TextHeadHistry(taskEntity = task)
                TextContentHistory(taskEntity = task)
            }
        }
    }
}

@Composable
private fun TextHeadHistry(
    taskEntity: TaskEntity
) {
    Text(
        modifier = Modifier.padding(end = 8.dp),
        text = taskEntity.entryDate.toStringFormatDate(),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
private fun TextContentHistory(
    taskEntity: TaskEntity
) {
    Text(
        text = taskEntity.title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}
