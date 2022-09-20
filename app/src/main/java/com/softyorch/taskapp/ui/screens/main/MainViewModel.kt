package com.softyorch.taskapp.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskUseCase: TaskUseCases
) : ViewModel() {
    private val _taskEntityList = MutableLiveData<List<TaskEntity>>()
    val taskEntityList: LiveData<List<TaskEntity>> = _taskEntityList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _taskToDo = MutableLiveData(0)
    private val _taskDone = MutableLiveData(0)

    init {
        _isLoading.value = true
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            taskUseCase.getAllTask().flowOn(Dispatchers.IO).collect { list ->
                _taskEntityList.postValue(list)
                updateLists(list)

                _isLoading.postValue(false)
            }
        }
    }

    suspend fun updateTask(taskEntity: TaskEntity) {
        _isLoading.value = true
        val state = viewModelScope.launch {
            taskUseCase.updateTask(taskEntity = taskEntity)
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