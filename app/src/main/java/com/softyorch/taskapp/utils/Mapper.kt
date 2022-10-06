package com.softyorch.taskapp.utils

interface Mapper<F, T> {
    fun from(task: F): T
    fun to(task: T): F
}