package com.softyorch.taskapp.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    suspend fun getTaskId(id: String): Resource<Task> = repository.getTaskId(id = id)
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task = task) }
    fun removeTask(task: Task) = viewModelScope.launch { repository.deleteTask(task = task) }
}