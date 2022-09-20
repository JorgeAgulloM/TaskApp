package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import kotlinx.coroutines.flow.*

class GetAllTask (private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskEntity>> = repository.getAllTaskFromDatabase()
}


