package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import kotlinx.coroutines.flow.*

class GetAllTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskEntity>> {
        return repository.getAllTaskFromDatabase().map { task ->
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> task.sortedBy { it.entryDate }
                        is TaskOrder.Name -> task.sortedBy { it.title.lowercase() }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder){
                        is TaskOrder.Create -> task.sortedBy { it.entryDate }
                        is TaskOrder.Name -> task.sortedBy { it.title.lowercase() }
                    }
                }
            }
        }
    }
}


