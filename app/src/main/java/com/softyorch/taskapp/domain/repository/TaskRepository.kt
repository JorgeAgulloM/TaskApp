package com.softyorch.taskapp.domain.repository

import com.google.android.gms.common.api.Response
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.data.data.tasks.TaskDatabaseDao
import com.softyorch.taskapp.domain.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTasks(): Flow<List<Task>> =
        taskDatabaseDao.getTasks().flowOn(Dispatchers.IO).conflate()

    suspend fun getTaskId(id: String): Resource<Task> {
        val response = try {
            Resource.Loading(data = true)
            taskDatabaseDao.getTaskById(id = id)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun addTask(task: Task) = taskDatabaseDao.insert(task = task)
    suspend fun updateTask(task: Task) = taskDatabaseDao.update(task = task)
    suspend fun deleteTask(task: Task): Resource<Boolean> =
        try {
            Resource.Loading(data = true)
            taskDatabaseDao.deleteTask(task = task)
            Resource.Loading(data = false)
            Resource.Success(data = true)
        } catch (e:Exception){
            Resource.Loading(data = false)
            Resource.Error(data = false, message = e.message.toString())
        }

    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}