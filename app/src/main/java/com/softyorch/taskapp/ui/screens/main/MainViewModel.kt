package com.softyorch.taskapp.ui.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import com.softyorch.taskapp.domain.model.toDomain
import com.softyorch.taskapp.domain.model.toUI

import com.softyorch.taskapp.domain.taskUsesCase.GetAllTaskUseCase
import com.softyorch.taskapp.domain.taskUsesCase.UpdateTaskUseCase
import com.softyorch.taskapp.ui.model.TaskModel
import com.softyorch.taskapp.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val getAllTaskUseCase: GetAllTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {
    private val _taskEntityList = MutableLiveData<List<TaskModel>>()
    val taskEntityList: LiveData<List<TaskModel>> = _taskEntityList

    private val _taskList = MutableLiveData<List<TaskModel>>()


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _taskToDo = MutableLiveData(0)
    private val _taskDone = MutableLiveData(0)

    init {
        _isLoading.value = true
        //loadData()
        loading2()
    }

    private fun loading2() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllTaskUseCase()

            result.flowOn(Dispatchers.IO).collect{ list ->
                _taskEntityList.postValue(list)
                updateLists(list)
                Log.d("MAPPING2", "list -> $list")
            }

            Log.d("MAPPING2", "_taskEntityList -> ${taskEntityList.value}")
            _isLoading.postValue(false)

        }
    }

/*
    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTasks().distinctUntilChanged()
                .collect { listOfTasks ->
                    if (listOfTasks.isEmpty()) {
                        //TODO meter la barra de carga para mostrar al usuario
                        _isLoading.postValue(false)
                    } else {
                        listOfTasks.let { list ->
                            _taskEntityList.postValue(list)
                            updateLists(list)
                        }
                        _isLoading.postValue(false)
                    }
                }
        }
    }
*/

    suspend fun updateTask(taskModel: TaskModel) {
        _isLoading.value = true
        val state = viewModelScope.launch {
            //repository.updateTask(taskEntity = taskEntity)
            updateTaskUseCase(taskEntity = taskModel.toUI())
        }
        state.join()
        updateLists()

        //loadData()
        loading2()
    }

    private fun updateLists(listOfTaskEntities: List<TaskModel>? = _taskEntityList.value) {
        listOfTaskEntities?.let { list ->
            val listToDo = list.filter { task -> !task.checkState }.size
            val listDone = list.filter { task -> task.checkState }.size
            _taskToDo.postValue(listToDo)
            _taskDone.postValue(listDone)
        }
    }
}