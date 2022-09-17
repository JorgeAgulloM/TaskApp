package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.TaskRepository2
import com.softyorch.taskapp.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetAllTaskUseCase @Inject constructor(private val repository2: TaskRepository2) {
    suspend operator fun invoke(): Flow<List<Task>> =
        repository2.getAllTaskFromDatabase().distinctUntilChanged()

}
