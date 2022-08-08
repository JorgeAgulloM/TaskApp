package com.softyorch.taskapp.repository

import com.softyorch.taskapp.data.TaskDatabaseDao
import com.softyorch.taskapp.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {
    fun getAllTasks(): Flow<List<Task>> =
        taskDatabaseDao.getTasks().flowOn(Dispatchers.IO).conflate()
    suspend fun getTaskId(id: String) = taskDatabaseDao.getTaskById(id = id)
    suspend fun addTask(task: Task) = taskDatabaseDao.insert(task = task)
    suspend fun updateTask(task: Task) = taskDatabaseDao.update(task = task)
    suspend fun deleteTask(task: Task) = taskDatabaseDao.deleteTask(task = task)
    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}