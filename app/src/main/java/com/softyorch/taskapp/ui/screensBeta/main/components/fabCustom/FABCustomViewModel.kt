/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.domain.taskUsesCase.mapToModelUseCases
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors.ErrorsNewTaskModel
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors.WithOutErrorsNewTask
import com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.model.NewTaskModel
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val taskUseCase: TaskUseCases
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
                _userName.postValue(user.username)
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

    private fun getData() = datastore.getData()

    private suspend fun addNewTask(newTaskModel: NewTaskModel) {
        taskUseCase.addNewTask(newTaskModel.mapToModelUseCases())
    }

}