package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskModel
import com.softyorch.taskapp.ui.models.NewTaskModel
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelUseCase(
    var id: UUID? = null,
    var title: String = emptyString,
    var description: String = emptyString,
    var author: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun TaskModel.mapToTaskModelUseCase() = TaskModelUseCase(
    id = this.id,
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)

fun TaskModelUseCase.mapToTaskModel()  = TaskModel(
    id = this.id,
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)

fun NewTaskModel.mapToModelUseCases(): TaskModelUseCase =
    TaskModelUseCase(
        title = this.title,
        description = this.description,
        author = this.author,
        entryDate = this.entryDate
    )