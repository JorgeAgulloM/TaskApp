package com.softyorch.taskapp.data.repository.task

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
    suspend fun deleteTask(taskEntity: TaskEntity) =
        taskDatabaseDao.deleteTask(taskEntity = taskEntity)


    fun getAllTaskFromDatabase2(): Flow<List<TaskModel>> = taskDatabaseDao.getTasks().let { flow ->
        flow.map { list -> list.map { it.mapToTaskModel() } }
    }

    suspend fun getTaskById2(idTask: String): TaskModel =
        taskDatabaseDao.getTaskById(id = idTask).mapToTaskModel()

    suspend fun addTask2(taskModel: TaskModel) =
        taskDatabaseDao.insert(taskModel.mapToTaskEntity())

    suspend fun updateTask2(taskModel: TaskModel) =
        taskDatabaseDao.update(taskModel.mapToTaskEntity())

    suspend fun deleteTask2(taskModel: TaskModel) =
        taskDatabaseDao.deleteTask(taskModel.mapToTaskEntity())


    suspend fun deleteAllTask() = taskDatabaseDao.deleteAll()
}
