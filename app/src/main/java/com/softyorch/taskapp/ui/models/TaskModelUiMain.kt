package com.softyorch.taskapp.ui.models

import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.utils.Mapper
import com.softyorch.taskapp.utils.emptyString
import java.time.Instant
import java.util.*

data class TaskModelUiMain(
    var id: UUID? = null,
    var title: String = emptyString,
    var entryDate: Date = Date.from(Instant.now()),
    var finishDate: Date? = null,
    var checkState: Boolean = false
)

class TaskMapperMain: Mapper<TaskModelUseCase, TaskModelUiMain> {

    override fun from(task: TaskModelUseCase): TaskModelUiMain = TaskModelUiMain(
        id = task.id,
        title = task.title,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )
    override fun to(task: TaskModelUiMain): TaskModelUseCase = TaskModelUseCase(
        id = task.id!!,
        title = task.title,
        entryDate = task.entryDate,
        finishDate = task.finishDate,
        checkState = task.checkState
    )

}
