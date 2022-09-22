package com.softyorch.taskapp.domain.taskUsesCase

data class TaskUseCases(
    val getAllTask: GetAllTask,
    val getCheckedTask: GetCheckedTask,
    val getUncheckedTask: GetUncheckedTask,
    val getTaskId: GetTaskId,
    val addNewTask: AddNewTask,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,
    val deleteAllTask: DeleteAllTask
)
