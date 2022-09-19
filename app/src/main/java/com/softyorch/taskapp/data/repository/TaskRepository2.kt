package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository2 @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTaskFromDatabase(): Flow<List<TaskEntity>> = taskDatabaseDao.getTasks()

    suspend fun getTaskById2(idTask: String): TaskEntity = taskDatabaseDao.getTaskById(id = idTask)
    suspend fun addTask(taskEntity: TaskEntity) = taskDatabaseDao.insert(taskEntity = taskEntity)
    suspend fun updateTask(taskEntity: TaskEntity) = taskDatabaseDao.update(taskEntity = taskEntity)
    suspend fun deleteTask(taskEntity: TaskEntity): Resource<Boolean> {
        Resource.Loading(data = true)
        try {
            taskDatabaseDao.deleteTask(taskEntity = taskEntity)
            Resource.Loading(data = false)
        } catch (e: Exception) {
            Resource.Loading(data = false)
            Resource.Error(data = false, message = e.message.toString())
        }
        return Resource.Success(data = true)
    }

    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}