package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository

class AddNewTask (private val repository: TaskRepository) {
    //@Throws(InvalidClassException::class)
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository.addTask(taskEntity = taskEntity)

    suspend fun invoke2(taskModelUseCase: TaskModelUseCase) =
        repository.addTask2(taskModel = TaskMapper().to(task = taskModelUseCase))
}