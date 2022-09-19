package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository2
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository2: TaskRepository2){
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository2.deleteTask(taskEntity = taskEntity)
}