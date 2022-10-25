package com.softyorch.taskapp.ui.components.fabCustom

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.widgets.newTask.newTask
import com.softyorch.taskapp.utils.ELEVATION_DP
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun FABCustom() {

    val viewModel = hiltViewModel<FABCustomViewModel>()
    val userName: String by viewModel.user.observeAsState(initial = "")

    var openDialog by remember { mutableStateOf(false) }


    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 5) * 3
    val maxWidth = LocalConfiguration.current.screenWidthDp
    val calculateWidth = maxWidth - 32

    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    var open by remember { mutableStateOf(false) }
    rememberCoroutineScope().launch {
        delay(200)
        open = true
    }

    val height by animateIntAsState(
        targetValue = if (openDialog) calculateHeight else 55,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    val width by animateIntAsState(
        targetValue = if (openDialog) calculateWidth else 155,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .height(height.dp)
                .width(width.dp)
                .fillMaxSize()
                .background(brush = sheetBrush, shape = MaterialTheme.shapes.large),
            contentAlignment = Alignment.TopCenter
        ) {
            ButtonCustom(text = "Close", true) {
                openDialog = false
            }
        }

        ExtendedFloatingActionButton(
            text = {
                Text(text = stringResource(add_task))
            },
            icon = {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(add_task)
                )
            },
            shape = MaterialTheme.shapes.large,
            onClick = { openDialog = true }
        )
    }



}
