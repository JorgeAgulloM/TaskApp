/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.fabCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.core.ejmploAlarma.PushNotifications
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.taskUsesCase.mapToModelUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.errors.ErrorsNewTaskModel
import com.softyorch.taskapp.ui.screens.main.components.fabCustom.errors.WithOutErrorsNewTask
import com.softyorch.taskapp.ui.models.NewTaskModel
import com.softyorch.taskapp.ui.models.mapToUserModelUI
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val useCases: UserDataUseCases,
    private val taskUseCase: TaskUseCases,
    private val pushNotifications: PushNotifications
) : ViewModel(), WithOutErrorsNewTask {

    private val _userName = MutableLiveData(emptyString)
    val userName: LiveData<String> = _userName

    private val _newTask = MutableLiveData(NewTaskModel())
    val newTask: LiveData<NewTaskModel> = _newTask

    private val _errorsNewTask = MutableLiveData(ErrorsNewTaskModel())
    val errorsNewTask: LiveData<ErrorsNewTaskModel> = _errorsNewTask

    private val _foundError = MutableLiveData(false)

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _titleDeedCounter = MutableLiveData(0)
    val titleDeedCounter: LiveData<Int> = _titleDeedCounter
    val limitCharTittle = 30
    init {
        getUserName()
    }

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect { user ->
                _userName.postValue(user.userName)
            }
        }
    }

    fun onInputChanged(newTaskModel: NewTaskModel) {
        _newTask.value = newTaskModel
        _titleDeedCounter.value = newTaskModel.title.length
        if (_foundError.value == true) {
            _errorsNewTask.postValue(withOutErrors(newTaskModel))
        } else _errorsNewTask.postValue(
            ErrorsNewTaskModel(isActivatedButton = isActivated(newTaskModel))
        )
    }

    fun onDataSend(newTaskModel: NewTaskModel): Boolean {
        _isLoading.value = true
        withOutErrors(newTaskModel).let { error ->
            setErrors(error = error)
            if (!error.error) viewModelScope.launch(Dispatchers.IO) {
                addNewTask(newTaskModel)
                resetDataInput()

                /** esto soslo es una prueba para lanzar una notify*/
                pushNotifications.notifyNotification(
                    textCompact = newTaskModel.title,
                    bigText = newTaskModel.description
                )
            }
            _isLoading.value = false
            return error.error
        }
    }

    private fun setErrors(error: ErrorsNewTaskModel) {
        if (_foundError.value != true) _foundError.value = true
        _errorsNewTask.postValue(error)
    }

    private fun resetDataInput(){
        _newTask.postValue(NewTaskModel())
    }

    /** Data */

    private fun getData() = useCases.getUser().map {
        it.mapToUserModelUI()
    }

    private suspend fun addNewTask(newTaskModel: NewTaskModel) {
        taskUseCase.addNewTask(newTaskModel.mapToModelUseCases())
    }

}