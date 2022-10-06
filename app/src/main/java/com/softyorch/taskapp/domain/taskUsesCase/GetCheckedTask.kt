package com.softyorch.taskapp.domain.taskUsesCase

import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.task.TaskRepository
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.utils.TimeLimitAutoLogin
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.util.*

class GetCheckedTask(private val repository: TaskRepository) {
    operator fun invoke(
        taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ): Flow<List<TaskEntity>> {
        return repository.getAllTaskFromDatabase().map { task ->
            val tasksChecked = task
                .filter { it.checkState }
                .filter { it ->
                    val timeWeekInMillis = TimeLimitAutoLogin.OneWeek().time
                    val finish = it.finishDate?.time
                    val dif = finish?.let { Date.from(Instant.now()).time.minus(it) }
                    dif?.let { timeWeekInMillis.compareTo(it) } == 1
                }

            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksChecked.sortedBy { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksChecked.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedByDescending { it.finishDate }
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
            val tasksChecked = task
                .filter { it.checkState }
                .filter { it ->
                    val timeWeekInMillis = TimeLimitAutoLogin.OneWeek().time
                    val finish = it.finishDate?.time
                    val dif = finish?.let { Date.from(Instant.now()).time.minus(it) }
                    dif?.let { timeWeekInMillis.compareTo(it) } == 1
                }

            when (taskOrder.orderType) {
                is OrderType.Ascending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksChecked.sortedBy { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedBy { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedBy { it.finishDate }
                    }
                }
                is OrderType.Descending -> {
                    when (taskOrder) {
                        is TaskOrder.Create -> tasksChecked.sortedByDescending { it.entryDate }
                        is TaskOrder.Name -> tasksChecked.sortedByDescending { it.title.lowercase() }
                        is TaskOrder.Finish -> tasksChecked.sortedByDescending { it.finishDate }
                    }
                }
            }
        }
    }
}


