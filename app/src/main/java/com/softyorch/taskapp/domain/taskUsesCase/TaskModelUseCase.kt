package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskModel
import com.softyorch.taskapp.utils.Mapper
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelUseCase (
    var id: UUID? = null,
    var title: String = emptyString,
    var description: String = emptyString,
    var author: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

class TaskMapper: Mapper<TaskModel, TaskModelUseCase> {

    override fun from(task: TaskModel): TaskModelUseCase = TaskModelUseCase(
        id = task.id,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )
    override fun to(task: TaskModelUseCase): TaskModel = TaskModel(
        id = task.id!!,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )

}