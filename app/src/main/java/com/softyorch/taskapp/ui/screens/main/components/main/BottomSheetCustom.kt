/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.ContentStickyHeader
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.utils.SHEET_TRANSITION_ENTER
import com.softyorch.taskapp.utils.SHEET_TRANSITION_EXIT
import com.softyorch.taskapp.utils.SMALL_TOP_BAR_HEIGHT
import com.softyorch.taskapp.utils.extensions.toStringFormatDate

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomSheetCustom(
    taskList: List<TaskModelUi>,
    isVisible: Boolean,
    index: Int,
    onCheckedChange: (TaskModelUi) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = SHEET_TRANSITION_ENTER,
        exit = SHEET_TRANSITION_EXIT
    ) {
        Column(
            modifier = Modifier
                .padding(top = (SMALL_TOP_BAR_HEIGHT + 8).dp)
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large.copy(
                        topStart = CornerSize(3.dp),
                        topEnd = CornerSize(50.dp)
                    )
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (taskList.isNotEmpty()) {
                    //val list = taskList
                    val taskMap: Map<String, List<TaskModelUi>> =
                        taskList.filter {
                            when (index) {
                                0 -> {
                                    !it.checkState
                                }
                                1 -> {
                                    it.checkState
                                }
                                else -> it.title.length > 1
                            }
                        }.groupBy { it.entryDate.toStringFormatDate() }

                    LazyColumn(
                        modifier = Modifier,
                        state = lazyState,
                        verticalArrangement = Arrangement.Top,
                        content = {
                            taskMap.forEach { (published, taskEntityList) ->
                                stickyHeader {
                                    ContentStickyHeader(published = published)
                                }

                                items(taskEntityList) { task ->
                                    ElevatedCardCustom(task, scope) { onCheckedChange(it) }
                                }
                            }
                            item { Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {} }
                        }
                    )
                } else Text(text = stringResource(R.string.add_new_task))
            }
        }
    }
}