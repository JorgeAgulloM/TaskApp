package com.softyorch.taskapp.presentation.screens.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.TaskRepository
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = _isDeleted

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    suspend fun getTask(id: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _isLoading.postValue(true)
            getTaskId(id = id).let { task ->
                when (task) {
                    is Resource.Error -> showError(task.message.toString())
                    is Resource.Loading -> _isLoading.value = true
                    is Resource.Success -> _taskDetail.postValue(task.data)
                }
                _isLoading.postValue(false)
            }
        } catch (e: Exception) {
            showError("Error al recuperar la información de la tarea. " + e.message.toString())
            _isLoading.postValue(false)
        }
    }

    fun updateAndRefreshTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.postValue(true)
        var result = updateTask(task = task)
        result.join()
        result = getTask(id = task.id.toString())
        result.join()
        _isLoading.postValue(false)
    }

    fun errorShown() {
        _messageError.value = emptyString
        _error.value = false
    }

    private fun showError(message: String) {
        _messageError.postValue(message)
        _error.value = true
    }


    private suspend fun getTaskId(id: String): Resource<Task> = repository.getTaskId(id = id)
    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(task = task)
    }

    fun removeTask(task: Task) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            (repository.deleteTask(task = task).data == true).also {
                _isDeleted.postValue(it)
                _error.postValue(!it)
                _isLoading.postValue(false)
            }
        }
    }
}