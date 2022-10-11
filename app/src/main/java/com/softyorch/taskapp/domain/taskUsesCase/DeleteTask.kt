package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository

class DeleteTask (private val repository: TaskRepository){
    suspend operator fun invoke(taskModelUseCase: TaskModelUseCase) =
        repository.deleteTask(taskModel = taskModelUseCase.mapToTaskModel())
}