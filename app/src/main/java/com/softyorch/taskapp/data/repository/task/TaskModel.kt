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

class TaskMapper: Mapper<TaskEntity, TaskModel> {

    override fun from(task: TaskEntity): TaskModel = TaskModel(
        id = task.id,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )
    override fun to(task: TaskModel): TaskEntity = TaskEntity(
        id = task.id!!,
        title = task.title,
        description = task.description,
        author = task.author,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )

}
