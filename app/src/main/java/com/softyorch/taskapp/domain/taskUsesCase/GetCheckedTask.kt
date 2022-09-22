package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import kotlinx.coroutines.flow.*

class GetCheckedTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskEntity>> {
        return repository.getAllTaskFromDatabase().map { task ->
            val tasksChecked = task.filter { it.checkState }
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> tasksChecked.sortedBy { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> tasksChecked.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedByDescending { it.finishDate }
                    }
                }
            }
        }
    }
}


