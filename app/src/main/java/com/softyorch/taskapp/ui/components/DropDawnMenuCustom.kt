package com.softyorch.taskapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.screens.main.utils.OrderOptions
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.containerColorAnimation

@ExperimentalMaterial3Api
@Composable
fun dropDawnMenuCustom(onchangeOrder: (TaskOrder) -> Unit): TaskOrder {
    var expanded by remember { mutableStateOf(value = false) }
    var orderOption: TaskOrder = TaskOrder.Create(OrderType.Descending)

    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            imageVector = Icons.Rounded.List,
            contentDescription = "Order of task",
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            RowInfo(text = "Order by...", paddingStart = 8.dp , heightSize = 20.dp, style = MaterialTheme.typography.labelSmall)
            OrderOptions.listOrder.forEach { order ->
                var onClick by remember { mutableStateOf(value = false) }
                DropdownMenuItem(
                    text = {
                        Text(text = order, style = MaterialTheme.typography.labelLarge)
                    },
                    onClick = {
                        orderOption =
                            when (order) {
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
                        Icon(
                            imageVector = Icons.Rounded.Sort,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                )
                Divider(modifier = Modifier.padding(start = 32.dp, end = 8.dp))
            }
        }
    }

    return orderOption
}
