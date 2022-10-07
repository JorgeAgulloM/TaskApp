package com.softyorch.taskapp.ui.screens.history

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelHistory(
    var id: UUID? = null,
    var title: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var checkState: Boolean = false
)

fun TaskModelUseCase.mapToTaskModelHistory() = TaskModelHistory(
    id = this.id,
    title = this.title,
    entryDate = this.entryDate,
    checkState = this.checkState
)

fun TaskModelHistory.mapToTaskModelUseCase() = TaskModelUseCase(
    id = this.id!!,
    title = this.title,
    entryDate = this.entryDate,
    checkState = this.checkState
)