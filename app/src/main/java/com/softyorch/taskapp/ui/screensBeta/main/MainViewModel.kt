/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.utils.OrderType
import com.softyorch.taskapp.domain.utils.TaskOrder
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.models.TaskModelUiMain
import com.softyorch.taskapp.ui.models.mapToTaskModelUI
import com.softyorch.taskapp.ui.models.mapToTaskModelUseCase
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors.ErrorsNewTaskModel
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors.WithOutErrorsNewTask
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.model.NewTaskModel
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.model.mapToNewTaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: TaskUseCases,
    private val datastoreUseCases: DatastoreUseCases
) : ViewModel(), WithOutErrorsNewTask {
    private val _tasks = MutableLiveData(listOf(TaskModelUi.emptyTask))
    val tasks: LiveData<List<TaskModelUi>> = _tasks

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> = _isVisible

    private val _newTask = MutableLiveData<NewTaskModel>()
    val newTask: LiveData<NewTaskModel> = _newTask

    private val _errorsNewTask = MutableLiveData<ErrorsNewTaskModel>()
    val errorsNewTask: LiveData<ErrorsNewTaskModel> = _errorsNewTask

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _titleDeedCounter = MutableLiveData<Int>(0)
    val titleDeedCounter: LiveData<Int> = _titleDeedCounter
    val limitCharTittle = 30

    private val _foundError = MutableLiveData<Boolean>()

    init {
        getUserName()
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

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            getUser().collect { user ->
                _userName.postValue(user.username)
            }
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

    /** New Task */

    fun onTextFieldInputChanged(newTaskModel: NewTaskModel) {
        _newTask.value = newTaskModel
        _titleDeedCounter.value = newTaskModel.title.length
        if (_foundError.value == true) {
            _errorsNewTask.postValue(withOutErrors(newTaskModel))
        } else _errorsNewTask.postValue(
            ErrorsNewTaskModel(isActivatedButton = isActivated(newTaskModel))
        )
    }

    fun onTextInputSend(taskModelUiMain: TaskModelUiMain): Boolean {
        _isLoading.value = true
        withOutErrors(taskModelUiMain.mapToNewTaskModel()).let { error ->
            setErrors(error = error)
            if (!error.error) viewModelScope.launch(Dispatchers.IO) {
                addNewTask(taskModelUiMain)
            }
            _isLoading.value = false
            return error.error
        }
    }

    private fun setErrors(error: ErrorsNewTaskModel) {
        if (_foundError.value != true) _foundError.value = true
        _errorsNewTask.postValue(error)
    }


    /** Data */

    private fun getUser() = datastoreUseCases.getData()

    private fun getAllTask(taskOrder: TaskOrder = TaskOrder.Create(OrderType.Descending)) =
        useCases.getAllTask(taskOrder)

    private suspend fun updateTask(taskModelUi: TaskModelUi) {
        useCases.updateTask(taskModelUi.mapToTaskModelUseCase())
    }

    private suspend fun addNewTask(taskModelUiMain: TaskModelUiMain) {
        useCases.addNewTask(taskModelUiMain.mapToTaskModelUseCase())
    }

}