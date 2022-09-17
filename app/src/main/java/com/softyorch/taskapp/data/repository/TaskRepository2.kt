package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository2 @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    suspend fun getAllTaskFromDatabase(): Flow<List<Task>> {
        val response: Flow<List<TaskEntity>> = taskDatabaseDao.getTasks()
        return response.map { list -> list.map { task -> task.toDomain() } }
    }
}