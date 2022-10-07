package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.Mapper
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

class TaskMapper: Mapper<TaskModelUseCase, TaskModelUi> {

    override fun from(task: TaskModelUseCase): TaskModelUi = TaskModelUi(
        id = task.id,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )
    override fun to(task: TaskModelUi): TaskModelUseCase = TaskModelUseCase(
        id = task.id!!,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )

}