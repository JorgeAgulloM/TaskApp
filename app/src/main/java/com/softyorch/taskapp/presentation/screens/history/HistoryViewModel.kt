package com.softyorch.taskapp.presentation.screens.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.TaskRepository
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    init {
        getTask()
    }

    private fun getTask() {
        try {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllTasks().distinctUntilChanged()
                    .collect { listOfTasks ->
                        if (listOfTasks.isEmpty()) {
                            showError("Error, la lista está vacía")
                        } else {
                            _taskList.postValue(listOfTasks)
                        }
                    }
            }
        } catch (e: Exception) {
            showError("" + e.message.toString())
            _isLoading.value = false
        }
    }

    fun errorShown() {
        _messageError.value = emptyString
        _error.value = false
    }

    private fun showError(message: String) {
        _messageError.postValue(message)
        _error.postValue(true)
    }

}