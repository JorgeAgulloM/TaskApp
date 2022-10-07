package com.softyorch.taskapp.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.models.TaskMapperMain
import com.softyorch.taskapp.ui.models.TaskModelUiMain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskUseCase: TaskUseCases
) : ViewModel() {
    //private val _taskEntityList = MutableLiveData<List<TaskEntity>>()
    //val taskEntityList: LiveData<List<TaskEntity>> = _taskEntityList

    private val _tasksEntityListUnchecked = MutableLiveData<List<TaskModelUiMain>>()
    val tasksEntityListUnchecked: LiveData<List<TaskModelUiMain>> = _tasksEntityListUnchecked

    private val _tasksEntityListChecked = MutableLiveData<List<TaskModelUiMain>>()
    val tasksEntityListChecked: LiveData<List<TaskModelUiMain>> = _tasksEntityListChecked

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            loadTaskUnchecked(taskOrder = taskOrder)
            loadTaskChecked(taskOrder = taskOrder)

            _isLoading.postValue(false)
        }
    }

    private fun loadTaskUnchecked(taskOrder: TaskOrder) = viewModelScope.launch {
        taskUseCase.getUncheckedTask(taskOrder = taskOrder).flowOn(Dispatchers.IO)
            .collect { list -> _tasksEntityListUnchecked.postValue(list.map { taskModelUseCase ->
                TaskMapperMain().from(taskModelUseCase)
            }) }
    }

    private fun loadTaskChecked(taskOrder: TaskOrder) = viewModelScope.launch {
        taskUseCase.getCheckedTask(taskOrder = taskOrder).flowOn(Dispatchers.IO)
            .collect { list -> _tasksEntityListChecked.postValue(list.map { taskModelUseCase ->
                TaskMapperMain().from(taskModelUseCase)
            }) }
    }

    fun changeOrderUncheckedTask(taskOrder: TaskOrder) {
        viewModelScope.launch {
            loadTaskUnchecked(taskOrder = taskOrder)
        }
    }

    fun changeOrderCheckedTask(taskOrder: TaskOrder) {
        viewModelScope.launch {
            loadTaskChecked(taskOrder = taskOrder)
        }
    }

    suspend fun updateTask(taskModelUiMain: TaskModelUiMain) {
        _isLoading.value = true
        val state = viewModelScope.launch {
            taskUseCase.updateTask(taskModelUseCase = TaskMapperMain().to(task = taskModelUiMain))
        }
        state.join()

        loadData()
    }

}