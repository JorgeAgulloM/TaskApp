package com.softyorch.taskapp.ui.screens.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun HistoryScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HistoryViewModel>()
    val coroutineScope = rememberCoroutineScope()
    var enterDetails by remember { mutableStateOf(value = false) }
    var exitDetails by remember { mutableStateOf(value = false) }

    coroutineScope.launch {
        delay(100)
        if (!exitDetails) enterDetails = true
    }

    val slideCheckBox by enterDetails.intOffsetAnimation {
        if (!enterDetails)
            navController.navigate(AppScreensRoutes.MainScreen.route) {
                popUpTo(AppScreensRoutes.DetailScreen.route) {
                    inclusive = true
                    navController.backQueue.clear()
                }
            }
    }
    val alphaAnimation: Float by exitDetails.alphaAnimation()
    val colorAnimation by exitDetails.containerColorAnimation()
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            //.padding(8.dp)
            .offset { slideCheckBox }
            .graphicsLayer(alpha = alphaAnimation)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    exitDetails = true
                    enterDetails = false
                },
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .background(color = colorAnimation, shape = MaterialTheme.shapes.large),
                //colors = IconButtonDefaults.iconButtonColors(containerColor = containerColor)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(
                        R.string.go_to_home
                    )
                )
            }
            RowInfo(text = "Historial", paddingStart = 30.dp, style = MaterialTheme.typography.titleLarge)
        }
        Content(navController = navController, viewModel = viewModel)
    }
}

@Composable
private fun Content(
    navController: NavHostController,
    viewModel: HistoryViewModel
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)

    val taskEntities: List<TaskEntity> by viewModel.taskEntityList.observeAsState(initial = emptyList())

    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )

    ) {
        item { Spacer(modifier = Modifier.padding(8.dp)) }
        items(taskEntities) { task ->
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 8.dp)
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
        modifier = Modifier.padding(horizontal = 8.dp),
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
