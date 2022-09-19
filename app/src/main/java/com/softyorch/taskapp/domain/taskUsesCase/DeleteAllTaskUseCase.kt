package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.TaskRepository
import javax.inject.Inject

class DeleteAllTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.deleteAllTask()
}