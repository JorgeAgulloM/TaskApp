/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.common.dropDawnMenuCustom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.extensions.containerColorAnimation

@ExperimentalMaterial3Api
@Composable
fun dropDawnMenuCustom(isFinish: Boolean, onchangeOrder: (TaskOrder) -> Unit): TaskOrder {
    var expanded by remember { mutableStateOf(value = false) }
    var orderOption: TaskOrder = TaskOrder.Create(OrderType.Descending)

    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            imageVector = if (expanded) Icons.Rounded.ViewList else Icons.Rounded.List,
            contentDescription = "Order of task",
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            RowInfo(
                text = "Order by...",
                paddingStart = 8.dp,
                heightSize = 20.dp,
                style = MaterialTheme.typography.labelSmall
            )
            OrderOptions.let { order ->
                OrderOptions.listOrder.forEach { orderText ->
                    if (!isFinish && (orderText == OrderOptions.listOrder[2] || orderText == OrderOptions.listOrder[5])) {
                        /** The option is not shown because it is not necessary*/
                    } else {
                        var onClick by remember { mutableStateOf(value = false) }
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = orderText,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            onClick = {
                                orderOption =
                                    when (orderText) {
                                        OrderOptions.listOrder[0] -> OrderOptions.CreateAscending().order
                                        OrderOptions.listOrder[1] -> OrderOptions.FinishAscending().order
                                        OrderOptions.listOrder[2] -> OrderOptions.NameAscending().order
                                        OrderOptions.listOrder[3] -> OrderOptions.CreateDescending().order
                                        OrderOptions.listOrder[4] -> OrderOptions.FinishDescending().order
                                        OrderOptions.listOrder[5] -> OrderOptions.NameDescending().order
                                        else -> {
                                            OrderOptions.CreateAscending().order
                                        }
                                    }
                                onchangeOrder(orderOption)
                                onClick = true
                            },
                            modifier = Modifier.height(40.dp).padding(4.dp).background(
                                color = onClick.containerColorAnimation {
                                    if (onClick) {
                                        expanded = false
                                        onClick = false
                                    }
                                }.value,
                                shape = MaterialTheme.shapes.large
                            ),
                            leadingIcon = {
                                val degrees = when (orderText) {
                                    OrderOptions.listOrder[3] -> 0f
                                    OrderOptions.listOrder[4] -> 0f
                                    OrderOptions.listOrder[5] -> 0f
                                    else -> {
                                        180f
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Rounded.Sort,
                                    contentDescription = null,
                                    modifier = Modifier.graphicsLayer(
                                        rotationY = degrees,
                                        rotationZ = degrees
                                    ),
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        )
                        Divider(modifier = Modifier.padding(start = 32.dp, end = 8.dp))
                    }
                }
            }
        }
    }

    return orderOption
}
