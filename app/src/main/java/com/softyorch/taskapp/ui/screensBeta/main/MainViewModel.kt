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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCases: TaskUseCases) : ViewModel() {
    private val _tasks = MutableLiveData<List<TaskModelUi>>(listOf(TaskModelUi.emptyTask))
    val tasks: LiveData<List<TaskModelUi>> = _tasks

    private val _isVisible = MutableLiveData<Boolean>(false)
    val isVisible: LiveData<Boolean> = _isVisible

    init {

    }

    fun load(taskLoad: TaskLoad){
        loadTask(taskLoad)
    }

    fun updateTask(taskModelUi: TaskModelUi){
        sendUpdateData(taskModelUi)
    }

    private fun visible(){
        viewModelScope.launch {
            delay(200)
            _isVisible.postValue(true)
        }
    }

    private fun loadTask(
        taskLoad: TaskLoad, taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isVisible.postValue(false)
            delay(200)
            when (taskLoad) {
                TaskLoad.AllTask -> useCases.getAllTask.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect { list ->
                        _tasks.postValue(list.map { taskModelUseCase ->
                            taskModelUseCase.mapToTaskModelUI()
                        })
                        visible()
                    }
                TaskLoad.CheckedTask -> useCases.getCheckedTask.invoke(taskOrder)
                    .flowOn(Dispatchers.IO)
                    .collect { list ->
                        _tasks.postValue(list.map { taskModelUseCase ->
                            taskModelUseCase.mapToTaskModelUI()
                        })
                        visible()
                    }
                TaskLoad.UncheckedTask -> useCases.getUncheckedTask.invoke(taskOrder)
                    .flowOn(Dispatchers.IO)
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
        useCases.updateTask.invoke(taskModelUi.mapToTaskModelUseCase())
    }

}