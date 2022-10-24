/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.extensions.intOffsetAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@Composable
fun CardTaskCustom(
    itemOfList: Int,
    task: TaskModelUi,
    isVisible: Boolean,
    onCheckedChange: (TaskModelUi, Int) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var isClickInCheckBox by remember { mutableStateOf(false) }
    var isMinCollapse by remember { mutableStateOf(value = false) }
    val scope = rememberCoroutineScope()

    val lines = 3
    val maxTextLength = 40
    val lineHeight = 20
    isMinCollapse = task.description.length / maxTextLength > lines

    if (!isVisible) isOpen = false
    if (!isVisible) isClickInCheckBox = false

    val cardOffsetAnim by isClickInCheckBox.intOffsetAnimation(!task.checkState, task.checkState) {
        if (isClickInCheckBox) {
            isClickInCheckBox = false
            onCheckedChange(
                task.copy(
                    checkState = !task.checkState,
                    finishDate = if (task.checkState) {
                        Date.from(Instant.now())
                    } else null
                ),
                itemOfList
            )
        }
    }

    ElevatedCard(
        modifier = Modifier
            .offset(cardOffsetAnim.x.dp, cardOffsetAnim.y.dp)
            .padding(
                start = 4.dp,
                end = 25.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(3.dp),
            topEnd = CornerSize(50.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp)
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = ELEVATION_DP
        )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    isOpen = !isOpen
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            DetailsTask(isMinCollapse, maxTextLength, lineHeight, task, isOpen) {
                scope.launch {
                    isOpen = false
                    delay(300)
                    isClickInCheckBox = true
                    //delay(1)
                    //onCheckedChange(task, itemOfList)
                }
            }
        }
    }

}