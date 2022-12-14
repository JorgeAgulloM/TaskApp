package com.softyorch.taskapp.ui.components.dropDawnMenuCustom

import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder

sealed class OrderOptions {
    data class CreateAscending(val order: TaskOrder = TaskOrder.Create(orderType = OrderType.Ascending)) :
        OrderOptions()

    data class FinishAscending(val order: TaskOrder = TaskOrder.Name(orderType = OrderType.Ascending)) :
        OrderOptions()

    data class NameAscending(val order: TaskOrder = TaskOrder.Finish(orderType = OrderType.Ascending)) :
        OrderOptions()

    data class CreateDescending(val order: TaskOrder = TaskOrder.Create(orderType = OrderType.Descending)) :
        OrderOptions()

    data class FinishDescending(val order: TaskOrder = TaskOrder.Name(orderType = OrderType.Descending)) :
        OrderOptions()

    data class NameDescending(val order: TaskOrder = TaskOrder.Finish(orderType = OrderType.Descending)) :
        OrderOptions()

    companion object {
        val listOrder = listOf(
            "Create ascending",
            "Name ascending",
            "Finish ascending",
            "Create descending",
            "Name descending",
            "Finish descending"
        )
    }
}

