package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    //@Throws(InvalidClassException::class)
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository.addTask(taskEntity = taskEntity)
}