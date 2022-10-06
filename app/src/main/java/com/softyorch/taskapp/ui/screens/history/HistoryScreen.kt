package com.softyorch.taskapp.ui.screens.history

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.ui.components.ContentStickyHeader
import com.softyorch.taskapp.ui.components.dropDawnMenuCustom
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
    val slideHead by enterDetails.intOffsetAnimationTransition(durationMillis = 100){}
    val slideCheckBox by enterDetails.intOffsetAnimationTransition {
        if (!enterDetails)
            navController.navigate(AppScreensRoutes.MainScreen.route) {
                popUpTo(AppScreensRoutes.DetailScreen.route) {
                    inclusive = true
                    navController.backQueue.clear()
                }
            }
    }

    coroutineScope.launch {
        delay(100)
        if (!exitDetails) enterDetails = true
    }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

    ) {
        val colorAnimation by exitDetails.containerColorAnimation()
        Row(
            modifier = Modifier.fillMaxWidth().offset { slideHead },
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
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(
                        go_to_home
                    )
                )
            }
            RowInfo(
                text = stringResource(history),
                paddingStart = 30.dp,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Content(navController = navController, viewModel = viewModel, modifier = Modifier.offset { slideCheckBox })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
private fun Content(
    navController: NavHostController,
    viewModel: HistoryViewModel,
    modifier: Modifier
) {
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val messageError: String by viewModel.messageError.observeAsState(initial = emptyString)
    val taskEntities: List<TaskModelHistory> by viewModel.taskEntityList.observeAsState(initial = emptyList())
    val taskMap: Map<String, List<TaskModelHistory>> =
        taskEntities.groupBy { it.entryDate.toStringFormatDate() }

    if (error) LocalContext.current.toastError(messageError) { viewModel.errorShown() }
    LazyColumn(
        modifier = modifier.fillMaxSize()
            .padding(8.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )

    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                dropDawnMenuCustom(isFinish = true) { viewModel.changeOrderTask(it) }
            }
        }

        taskMap.forEach { (published, taskList) ->
            stickyHeader {
                ContentStickyHeader(published = published)
            }

            items(taskList) { task ->
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .clickable {
                            navController.navigate(AppScreens.DetailsScreen.name + "/${task.id}")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextHeadHistry(
                        entryDate = task.entryDate.toStringFormatDate(),
                        isFinish = task.checkState
                    )
                    TextContentHistory(title = task.title)
                }
            }
        }
    }
}

@Composable
private fun TextHeadHistry(
    entryDate: String,
    isFinish: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        if (isFinish) Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = stringResource(content_is_task_finished),
            tint = MaterialTheme.colorScheme.primary
        )
        else Box(modifier = Modifier.size(24.dp)) {}
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = entryDate,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun TextContentHistory(
    title: String
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}
