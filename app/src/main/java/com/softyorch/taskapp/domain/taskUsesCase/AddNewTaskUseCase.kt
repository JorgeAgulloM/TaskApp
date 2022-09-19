package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository2
import java.io.InvalidClassException
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor(private val repository2: TaskRepository2) {
    //@Throws(InvalidClassException::class)
    suspend operator fun invoke(taskEntity: TaskEntity) =
        repository2.addTask(taskEntity = taskEntity)
}