package com.softyorch.taskapp.data.repository.task

import com.softyorch.taskapp.data.database.tasks.TaskDatabaseDao
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDatabaseDao: TaskDatabaseDao) {

    fun getAllTaskFromDatabase(): Flow<List<TaskModel>> = taskDatabaseDao.getTasks().let { flow ->
        flow.map { list -> list.map { it.mapToTaskModel() } }
    }

    suspend fun getTaskById(idTask: String): TaskModel =
        taskDatabaseDao.getTaskById(id = idTask).mapToTaskModel()

    suspend fun addTask(taskModel: TaskModel) =
        taskDatabaseDao.insert(taskModel.mapToTaskEntity())

    suspend fun updateTask(taskModel: TaskModel) =
        taskDatabaseDao.update(taskModel.mapToTaskEntity())

    suspend fun deleteTask(taskModel: TaskModel) =
        taskDatabaseDao.deleteTask(taskModel.mapToTaskEntity())

    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}
