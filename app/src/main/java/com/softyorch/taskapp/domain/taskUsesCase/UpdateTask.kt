package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository

class UpdateTask (private val repository: TaskRepository) {
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository.updateTask(taskEntity = taskEntity)
}