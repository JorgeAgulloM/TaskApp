package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelUiMain(
    var id: UUID? = null,
    var title: String = emptyString,
    var description: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

fun TaskModelUseCase.mapToTaskModelUiMain() = TaskModelUiMain(
    id = this.id!!,
    title = this.title,
    description = this.description,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)

fun TaskModelUiMain.mapToTaskModelUseCase() = TaskModelUseCase(
    id = this.id!!,
    title = this.title,
    description = this.description,
    entryDate = this.entryDate,
    finishDate = this.finishDate,
    checkState = this.checkState
)
