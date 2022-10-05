package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class DeleteAllTask (private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.deleteAllTask()
}