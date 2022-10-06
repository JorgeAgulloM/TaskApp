package com.softyorch.taskapp.ui.screens.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val taskUseCase: TaskUseCases
) : ViewModel() {
    private val _taskEntityList = MutableLiveData<List<TaskModelHistory>>()
    val taskEntityList: LiveData<List<TaskModelHistory>> = _taskEntityList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    init {
        getTask()
    }

    private fun getTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) {
        try {
            _isLoading.value = true
            viewModelScope.launch() {
                taskUseCase.getAllTask(taskOrder = taskOrder).flowOn(Dispatchers.IO)
                    .collect { list ->
                        if (list.isEmpty()) {
                            showError("Error, la lista está vacía")
                        } else {
                            _taskEntityList.postValue(list.map { taskModelUseCase ->
                                TaskMapper().from(task = taskModelUseCase)
                            })
                        }
                    }
            }
        } catch (e: Exception) {
            showError("" + e.message.toString())
            _isLoading.value = false
        }
    }

    fun changeOrderTask(taskOrder: TaskOrder) {
        getTask(taskOrder = taskOrder)
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