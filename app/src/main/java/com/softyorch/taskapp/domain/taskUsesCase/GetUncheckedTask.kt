package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import kotlinx.coroutines.flow.*

class GetUncheckedTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskEntity>> {
        return repository.getAllTaskFromDatabase().map { task ->
            val tasksUnchecked = task.filter { !it.checkState }
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> tasksUnchecked.sortedBy { it.entryDate }
                        is TaskOrder.Name -> tasksUnchecked.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksUnchecked.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> tasksUnchecked.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> tasksUnchecked.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksUnchecked.sortedByDescending { it.finishDate }
                    }
                }
            }
        }
    }

    fun invoke2(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskModelUseCase>> {
        val taskListResponse = repository.getAllTaskFromDatabase2().map { list ->
            list.map { taskModel -> TaskMapper().from(task = taskModel) }
        }

        return taskListResponse.map { task ->
            val tasksUnchecked = task.filter { !it.checkState }
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksUnchecked.sortedBy { it.entryDate }
                        is TaskOrder.Name -> tasksUnchecked.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksUnchecked.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksUnchecked.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> tasksUnchecked.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksUnchecked.sortedByDescending { it.finishDate }
                    }
                }
            }
        }
    }

}


