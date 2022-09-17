package com.softyorch.taskapp.ui.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.data.repository.TaskRepository
import com.softyorch.taskapp.data.repository.TaskRepository2
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.taskUsesCase.GetAllTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val getAllTaskUseCase: GetAllTaskUseCase
) : ViewModel() {
    private val _taskEntityList = MutableLiveData<List<TaskEntity>>()
    val taskEntityList: LiveData<List<TaskEntity>> = _taskEntityList

    private val _taskList = MutableLiveData<List<Task>>()


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _taskToDo = MutableLiveData(0)
    private val _taskDone = MutableLiveData(0)

    init {
        _isLoading.value = true
        loadData()
        loading2()
    }

    private fun loading2() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAllTaskUseCase()

            result.distinctUntilChanged().collect { listOfTask ->
                if (listOfTask.isNotEmpty())
                    listOfTask.let { list ->
                        _taskList.postValue(list)
                    }
                _isLoading.postValue(false)
                delay(500)
                Log.d("LISTTWO", "ListOfTask -> ${_taskList.value}")
            }
        }
    }

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

    suspend fun updateTask(taskEntity: TaskEntity) {
        _isLoading.value = true
        val state = viewModelScope.launch {
            repository.updateTask(taskEntity = taskEntity)
        }
        state.join()
        updateLists()

        loadData()
    }

    private fun updateLists(listOfTaskEntities: List<TaskEntity>? = _taskEntityList.value) {
        listOfTaskEntities?.let { list ->
            val listToDo = list.filter { task -> !task.checkState }.size
            val listDone = list.filter { task -> task.checkState }.size
            _taskToDo.postValue(listToDo)
            _taskDone.postValue(listDone)
        }
    }
}