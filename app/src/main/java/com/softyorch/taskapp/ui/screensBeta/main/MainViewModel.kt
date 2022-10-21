/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.models.mapToTaskModelUI
import com.softyorch.taskapp.ui.models.mapToTaskModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCases: TaskUseCases) : ViewModel() {
    private val _tasks = MutableLiveData(listOf(TaskModelUi.emptyTask))
    val tasks: LiveData<List<TaskModelUi>> = _tasks

    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _isVisible

    init {
        loadTask()
        visible()
    }

    fun updateTasks(taskModelUi: TaskModelUi) {
        sendUpdateData(taskModelUi)
    }

    fun visible() {
        viewModelScope.launch {
            delay(0)
            _isVisible.postValue(false)
            delay(400)
            _isVisible.postValue(true)
        }
    }

    fun dropTaskLocalList(taskModelUi: TaskModelUi) {
        val list = _tasks.value as MutableList
        list.removeIf { it == taskModelUi }
        _tasks.value = list
    }

    @OptIn(FlowPreview::class)
    private fun loadTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) {
        viewModelScope.launch(Dispatchers.IO) {
            _isVisible.postValue(false)
            val debounceTime = 500L
            getAllTask(taskOrder)
                .debounce(debounceTime)
                .collect { list ->
                    _tasks.postValue(list.map { taskModelUseCase ->
                        taskModelUseCase.mapToTaskModelUI()
                    })
                }
        }
    }

    private fun sendUpdateData(taskModelUi: TaskModelUi) = viewModelScope.launch(Dispatchers.IO) {
        updateLocalTaskList(taskModelUi)
        updateTask(taskModelUi)
    }

    private fun updateLocalTaskList(taskModelUi: TaskModelUi) = viewModelScope.launch {
        val newList: MutableList<TaskModelUi> = mutableListOf()
        newList.addAll(_tasks.value!!)

        newList.forEach {
            if (it.id == taskModelUi.id) {
                it.checkState = taskModelUi.checkState
            }
        }

        _tasks.postValue(newList)
    }

    /** Data */

    private fun getAllTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) =
        useCases.getAllTask(taskOrder)

    private fun getCheckedTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) =
        useCases.getCheckedTask(taskOrder)

    private fun getUncheckedTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) =
        useCases.getUncheckedTask(taskOrder)

    private suspend fun updateTask(taskModelUi: TaskModelUi) =
        useCases.updateTask(taskModelUi.mapToTaskModelUseCase())

}