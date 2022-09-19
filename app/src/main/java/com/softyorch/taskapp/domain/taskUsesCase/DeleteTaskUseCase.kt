package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository){
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository.deleteTask(taskEntity = taskEntity)
}