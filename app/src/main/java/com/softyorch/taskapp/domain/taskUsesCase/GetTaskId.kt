package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.utils.DataOrError

class GetTaskId(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String): DataOrError<TaskModelUseCase, String> {
        val response = DataOrError<TaskModelUseCase, String>()
        try {
            response.data = repository.getTaskById2(idTask = taskId).mapToTaskModelUseCase()
        } catch (ex: Exception) {
            response.error = ex.message.toString()
        }

        return response
    }
}