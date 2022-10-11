package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class AddNewTask (private val repository: TaskRepository) {
    suspend operator fun invoke(taskModelUseCase: TaskModelUseCase) =
        repository.addTask(taskModel = taskModelUseCase.mapToTaskModel())
}