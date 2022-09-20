package com.softyorch.taskapp.domain.taskUsesCase

data class TaskUseCases(
    val addNewTask: AddNewTask,
    val deleteTask: DeleteTask,
    val deleteAllTask: DeleteAllTask,
    val getAllTask: GetAllTask,
    val getTaskId: GetTaskId,
    val updateTask: UpdateTask
)
