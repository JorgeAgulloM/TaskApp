package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class UpdateTask (private val repository: TaskRepository) {
    suspend operator fun invoke(taskModelUseCase: TaskModelUseCase) =
        repository.updateTask2(taskModel = taskModelUseCase.mapToTaskModel())
}