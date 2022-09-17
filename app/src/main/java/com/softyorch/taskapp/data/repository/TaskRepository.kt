package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTasks(): Flow<List<TaskEntity>> =
        taskDatabaseDao.getTasks().flowOn(Dispatchers.IO).conflate()

    suspend fun getTaskId(id: String): Resource<TaskEntity> {
        val response = try {
            Resource.Loading(data = true)
            taskDatabaseDao.getTaskById(id = id)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun addTask(taskEntity: TaskEntity) = taskDatabaseDao.insert(taskEntity = taskEntity)
    suspend fun updateTask(taskEntity: TaskEntity) = taskDatabaseDao.update(taskEntity = taskEntity)
    suspend fun deleteTask(taskEntity: TaskEntity): Resource<Boolean> =
        try {
            Resource.Loading(data = true)
            taskDatabaseDao.deleteTask(taskEntity = taskEntity)
            Resource.Loading(data = false)
            Resource.Success(data = true)
        } catch (e:Exception){
            Resource.Loading(data = false)
            Resource.Error(data = false, message = e.message.toString())
        }

    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}