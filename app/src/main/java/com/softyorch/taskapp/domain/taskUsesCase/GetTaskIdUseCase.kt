package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository2
import javax.inject.Inject

class GetTaskIdUseCase @Inject constructor(private val repository2: TaskRepository2) {
    suspend operator fun invoke(taskId: String): Resource<TaskEntity> {
        val response = try {
            Resource.Loading(data = true)
            repository2.getTaskById2(idTask = taskId)
        } catch (exception: Exception) {
            Resource.Loading(data = false)
            return Resource.Error(data = null, message = exception.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}