package com.softyorch.taskapp.ui.model

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.model.Task
import java.time.Instant
import java.util.*

data class TaskModel(
    val id: UUID,
    var title: String,
    var description: String,
    val author: String,
    val entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun Task.toUI() = TaskModel(id, title, description, author, entryDate, finishDate, checkState)
fun TaskEntity.toUI() = TaskModel(id, title, description, author, entryDate, finishDate, checkState)
