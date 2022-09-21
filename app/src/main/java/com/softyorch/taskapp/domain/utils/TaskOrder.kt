package com.softyorch.taskapp.domain.utils

sealed class TaskOrder(val orderType: OrderType){
    class Create(orderType: OrderType): TaskOrder(orderType = orderType)
    class Name(orderType: OrderType): TaskOrder(orderType = orderType)
}

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}
