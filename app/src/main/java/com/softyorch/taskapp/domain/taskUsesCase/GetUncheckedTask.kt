package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import kotlinx.coroutines.flow.*

class GetUncheckedTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskModelUseCase>> {
        return repository.getAllTaskFromDatabase2().map { task ->
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
        }.map { list -> list.map { taskModel -> taskModel.mapToTaskModelUseCase() } }
    }

}


