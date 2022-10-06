package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.utils.DataOrError

class GetTaskId (private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: String): DataOrError<TaskEntity, String> {
        val response = DataOrError<TaskEntity, String>()
        try {
            response.data = repository.getTaskById(idTask = taskId)
        } catch (ex: Exception) {
            response.error = ex.message.toString()
        }

        return response
    }

    suspend fun invoke2(taskId: String): DataOrError<TaskModelUseCase, String> {
        val response = DataOrError<TaskModelUseCase, String>()
        try {
            response.data = TaskMapper().from(task = repository.getTaskById2(idTask = taskId))
        } catch (ex: Exception) {
            response.error = ex.message.toString()
        }

        return response
    }
}