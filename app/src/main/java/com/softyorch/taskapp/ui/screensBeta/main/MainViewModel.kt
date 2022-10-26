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
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors.WithOutErrorsNewTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCases: TaskUseCases) : ViewModel(), WithOutErrorsNewTask {
    private val _tasks = MutableLiveData(listOf(TaskModelUi.emptyTask))
    val tasks: LiveData<List<TaskModelUi>> = _tasks

    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _isVisible

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadTask()
        visible()
    }

    fun updateTasks(taskModelUi: TaskModelUi) {
        sendUpdateData(taskModelUi)
    }

    fun hideSheet() {
        _isVisible.value = isVisible.value != true
    }

    fun visible() {
        viewModelScope.launch {
            delay(0)
            _isVisible.postValue(false)
            delay(400)
            _isVisible.postValue(true)
        }
    }

    @OptIn(FlowPreview::class)
    private fun loadTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) {
        viewModelScope.launch(Dispatchers.IO) {
            _isVisible.postValue(false)
            /** Para añadir datos fake
            useCases.fakeData()
            delay(2000)
             */
            val debounceTime = 200L
            getAllTask(taskOrder)
                .debounce(debounceTime)
                .collect { list ->
                    _tasks.postValue(list.map { taskModelUseCase ->
                        taskModelUseCase.mapToTaskModelUI()
                    })
                }
        }
    }

    private fun sendUpdateData(taskModelUi: TaskModelUi) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTask(taskModelUi)
        }
    }

    /** Data */

    private fun getAllTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) =
        useCases.getAllTask(taskOrder)

    private suspend fun updateTask(taskModelUi: TaskModelUi) {
        useCases.updateTask(taskModelUi.mapToTaskModelUseCase())
    }

}