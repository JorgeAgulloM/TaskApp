package com.softyorch.taskapp.ui.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _taskEntityDetail = MutableLiveData<TaskEntity>()
    val taskEntityDetail: LiveData<TaskEntity> = _taskEntityDetail

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
                    is Resource.Success -> _taskEntityDetail.postValue(task.data)
                }
                _isLoading.postValue(false)
            }
        } catch (e: Exception) {
            showError("Error al recuperar la información de la tarea. " + e.message.toString())
            _isLoading.postValue(false)
        }
    }

    fun updateAndRefreshTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.postValue(true)
        var result = updateTask(taskEntity = taskEntity)
        result.join()
        result = getTask(id = taskEntity.id.toString())
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


    private suspend fun getTaskId(id: String): Resource<TaskEntity> = repository.getTaskId(id = id)
    fun updateTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(taskEntity = taskEntity)
    }

    fun removeTask(taskEntity: TaskEntity) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            (repository.deleteTask(taskEntity = taskEntity).data == true).also {
                _isDeleted.postValue(it)
                _error.postValue(!it)
                _isLoading.postValue(false)
            }
        }
    }
}