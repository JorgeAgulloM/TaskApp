package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.TaskRepository2
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.model.toDomain
import javax.inject.Inject

class GetTaskIdUseCase @Inject constructor(private val repository2: TaskRepository2) {
    suspend operator fun invoke(taskId: String): Task? =
        repository2.getTaskId(id = taskId).data?.toDomain()
}