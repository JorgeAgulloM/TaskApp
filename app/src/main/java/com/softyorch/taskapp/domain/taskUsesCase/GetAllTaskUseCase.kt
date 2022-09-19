package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository2
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllTaskUseCase @Inject constructor(private val repository2: TaskRepository2) {
    operator fun invoke(): Flow<List<TaskEntity>> = repository2.getAllTaskFromDatabase()
}


