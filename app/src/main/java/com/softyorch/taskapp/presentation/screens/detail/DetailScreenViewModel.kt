package com.softyorch.taskapp.presentation.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _taskDetail = MutableLiveData<Task>()
    val taskDetail: LiveData<Task> = _taskDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun getTask(id: String) = viewModelScope.launch {
        _isLoading.value = true
        getTaskId(id = id).let { data ->
            data.data?.let { task -> _taskDetail.postValue(task) }
        }

        _isLoading.value = false
    }

    fun updateAndRefreshTask(task: Task) = viewModelScope.launch {
        _isLoading.value = true
        var result = updateTask(task = task)
        result.join()
        result = getTask(id = task.id.toString())
        result.join()
        _isLoading.value = false
    }


    private suspend fun getTaskId(id: String): Resource<Task> = repository.getTaskId(id = id)
    fun updateTask(task: Task) = viewModelScope.launch { repository.updateTask(task = task) }
    fun removeTask(task: Task) = viewModelScope.launch { repository.deleteTask(task = task) }

}