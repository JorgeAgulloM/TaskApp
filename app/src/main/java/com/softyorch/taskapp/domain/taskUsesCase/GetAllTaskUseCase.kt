package com.softyorch.taskapp.domain.taskUsesCase

import android.util.Log
import com.softyorch.taskapp.data.repository.TaskRepository2
import com.softyorch.taskapp.ui.model.TaskModel
import com.softyorch.taskapp.ui.model.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllTaskUseCase @Inject constructor(private val repository2: TaskRepository2) {
    suspend operator fun invoke(): Flow<List<TaskModel>> {
        val result = repository2.getAllTaskFromDatabase()
        return result.map { listOfTask ->
            listOfTask.map {
                it.toUI()
            }
        }
    }
}


