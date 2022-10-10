package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class GetTaskId(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String): TaskModelUseCase =
        repository.getTaskById(idTask = taskId).mapToTaskModelUseCase()

}