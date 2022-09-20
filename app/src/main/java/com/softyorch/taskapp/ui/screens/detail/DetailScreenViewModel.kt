package com.softyorch.taskapp.ui.screens.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val taskUseCase: TaskUseCases

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
            getTaskId(id = id)
        } catch (e: Exception) {
            if (isDeleted.value == false) showError("Error al recuperar la información de la tarea. " + e.message.toString())
            _isLoading.postValue(false)
        }
        _isLoading.postValue(false)
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
        _error.postValue(true)
    }

    private suspend fun getTaskId(id: String) {
        when (val result = taskUseCase.getTaskId(taskId = id)) {
            is Resource.Error -> showError(result.message.toString())
            is Resource.Loading -> _isLoading.value = true
            is Resource.Success -> _taskEntityDetail.postValue(result.data!!)
        }
    }

    fun updateTask(taskEntity: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        taskUseCase.updateTask(taskEntity = taskEntity)
    }

    fun removeTask(taskEntity: TaskEntity) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            taskUseCase.deleteTask(taskEntity = taskEntity).also {
                _isDeleted.postValue(true)
                _isLoading.postValue(false)
            }
        }
    }
}