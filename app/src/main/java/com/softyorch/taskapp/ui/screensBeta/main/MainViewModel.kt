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

    }

    fun load(taskLoad: TaskLoad) {
        loadTask(taskLoad)
    }

    fun updateTasks(taskModelUi: TaskModelUi) {
        sendUpdateData(taskModelUi)
    }

    private fun visible() {
        viewModelScope.launch {
            delay(200)
            _isVisible.postValue(true)
        }
    }

    @OptIn(FlowPreview::class)
    private fun loadTask(
        taskLoad: TaskLoad, taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isVisible.postValue(false)
            val debounceTime = 500L
            //delay(200)
            when (taskLoad) {
                TaskLoad.AllTask -> getAllTask(taskOrder)
                    .debounce(debounceTime)
                    .collect { list ->
                        _tasks.postValue(list.map { taskModelUseCase ->
                            taskModelUseCase.mapToTaskModelUI()
                        })
                        visible()
                    }
                TaskLoad.CheckedTask -> getCheckedTask(taskOrder)
                    .debounce(debounceTime)
                    .collect { list ->
                        _tasks.postValue(list.map { taskModelUseCase ->
                            taskModelUseCase.mapToTaskModelUI()
                        })
                        visible()
                    }
                TaskLoad.UncheckedTask -> getUncheckedTask(taskOrder)
                    .debounce(debounceTime)
                    .collect { list ->
                        _tasks.postValue(list.map { taskModelUseCase ->
                            taskModelUseCase.mapToTaskModelUI()
                        })
                        visible()
                    }
            }
        }
    }

    private fun sendUpdateData(taskModelUi: TaskModelUi) = viewModelScope.launch(Dispatchers.IO) {
        updateLocalTaskList(taskModelUi)
        //delay(1000)
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