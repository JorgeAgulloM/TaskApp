package com.softyorch.taskapp.domain.model

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import java.time.Instant
import java.util.*

data class Task(
    var title: String,
    var description: String,
    val author: String,
    val entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun TaskEntity.toDomain() = Task(title, description, author, entryDate, finishDate, checkState)
