package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import kotlinx.coroutines.flow.*

class GetAllTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskModelUseCase>> {
        return repository.getAllTaskFromDatabase2().map { list ->
            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> list.sortedBy { it.entryDate }
                        is TaskOrder.Name -> list.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> list.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> list.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> list.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> list.sortedByDescending { it.finishDate }
                    }
                }
            }
        }.map { it ->
            when (taskOrder.orderType) {
                OrderType.Ascending -> it.sortedBy { it.checkState }
                OrderType.Descending -> it.sortedByDescending { it.checkState }
            }
        }.map { list -> list.map { taskModel -> taskModel.mapToTaskModelUseCase() } }
    }
}


