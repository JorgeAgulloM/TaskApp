package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelUi(
    var id: UUID? = null,
    var title: String = emptyString,
    var description: String = emptyString,
    var author: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun TaskModelUi.mapToTaskModelUseCase() = TaskModelUseCase(
    id = this.id ?: UUID.randomUUID(),
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)

fun TaskModelUseCase.mapToTaskModelUI() = TaskModelUi(
    id = this.id!!,
    title = this.title,
    description = this.description,
    author = this.author,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)