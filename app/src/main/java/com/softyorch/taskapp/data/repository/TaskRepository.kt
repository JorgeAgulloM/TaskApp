package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTaskFromDatabase(): Flow<List<TaskEntity>> = taskDatabaseDao.getTasks()

    suspend fun getTaskById(idTask: String): TaskEntity = taskDatabaseDao.getTaskById(id = idTask)
    suspend fun addTask(taskEntity: TaskEntity) = taskDatabaseDao.insert(taskEntity = taskEntity)
    suspend fun updateTask(taskEntity: TaskEntity) = taskDatabaseDao.update(taskEntity = taskEntity)
    suspend fun deleteTask(taskEntity: TaskEntity) = taskDatabaseDao.deleteTask(taskEntity = taskEntity)
    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}