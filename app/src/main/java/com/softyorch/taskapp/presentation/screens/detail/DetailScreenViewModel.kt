package com.softyorch.taskapp.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.TaskRepository
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    suspend fun getTaskId(id: String): Resource<Task> = repository.getTaskId(id = id)
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task = task) }
    fun removeTask(task: Task) = viewModelScope.launch { repository.deleteTask(task = task) }

    fun nameOfUserLogin(): String = stateLogin.userActive().username

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()

}