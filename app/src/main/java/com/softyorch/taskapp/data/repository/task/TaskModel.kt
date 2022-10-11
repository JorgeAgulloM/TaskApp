package com.softyorch.taskapp.data.repository.task

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.utils.*
import java.time.Instant
import java.util.*

data class TaskModel(
    var id: UUID? = null,
    var title: String = emptyString,
    var description: String = emptyString,
    var author: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun TaskEntity.mapToTaskModel() = TaskModel(
    id = this.id,
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)

fun TaskModel.mapToTaskEntity() = TaskEntity(
    id = this.id ?: UUID.randomUUID(),
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)
