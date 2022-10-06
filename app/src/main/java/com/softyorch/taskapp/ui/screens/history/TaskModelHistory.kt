package com.softyorch.taskapp.ui.screens.history

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.Mapper
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelHistory(
    var id: UUID? = null,
    var title: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var checkState: Boolean = false
)

class TaskMapper: Mapper<TaskModelUseCase, TaskModelHistory> {

    override fun from(task: TaskModelUseCase): TaskModelHistory = TaskModelHistory(
        id = task.id,
        title = task.title,
        entryDate = task.entryDate,
        checkState = task.checkState
    )
    override fun to(task: TaskModelHistory): TaskModelUseCase = TaskModelUseCase(
        id = task.id!!,
        title = task.title,
        entryDate = task.entryDate,
        checkState = task.checkState
    )
}