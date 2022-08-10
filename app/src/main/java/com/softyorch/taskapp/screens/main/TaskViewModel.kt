package com.softyorch.taskapp.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTasks().distinctUntilChanged()
                .collect { listOfTasks ->
                    if (listOfTasks.isNullOrEmpty()) {
                        //TODO meter la barra de carga para mostrar al usuario
                    } else {
                        _taskList.value = listOfTasks
                    }
                }
        }
    }

    //fun getTaskId(id: String) = viewModelScope.launch { repository.getTaskId(id = id) }
    suspend fun getTaskId(id: String): Resource<Task> = repository.getTaskId(id = id)
    fun addTask(task: Task) = viewModelScope.launch { repository.addTask(task = task) }
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task = task) }
    fun removeTask(task: Task) = viewModelScope.launch { repository.deleteTask(task = task) }

}