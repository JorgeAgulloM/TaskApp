package com.softyorch.taskapp.data

class DataOrException<T, Boolean, E : Exception> {
    var data: T? = null
    var loading: Boolean? = null
    var e: E? = null
}