package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.TaskRepository2
import javax.inject.Inject

class DeleteAllTaskUseCase @Inject constructor(private val repository2: TaskRepository2) {
    suspend operator fun invoke() = repository2.deleteAllTask()
}