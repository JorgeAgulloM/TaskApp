package com.softyorch.taskapp.presentation.screens.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.TaskRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _taskToDo = MutableLiveData<Int>(0)
    private val _taskDone = MutableLiveData<Int>(0)

    private val _showArrowToDO = MutableLiveData<Boolean>()
    val showArrowToDO: LiveData<Boolean> = _showArrowToDO

    private val _showArrowDone = MutableLiveData<Boolean>()
    val showArrowDone: LiveData<Boolean> = _showArrowDone

    init {
        _isLoading.value = true
        loadData()
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
                            _taskList.postValue(list)
                            updateLists(list)
                        }
                        _isLoading.postValue(false)
                    }
                }
        }
    }

    suspend fun updateTask(task: Task) {
        _isLoading.value = true
        val state = viewModelScope.launch {
            repository.updateTask(task = task)
        }
        state.join()
        updateLists()

        loadData()
    }

    private fun showOrNotArrows() {
        _taskToDo.let { _showArrowToDO.value = it.value!! > 7 }
        _taskDone.let { _showArrowDone.value = it.value!! > 7 }
    }

    private fun updateLists(listOfTasks: List<Task>? = _taskList.value) {
        Log.d("LISTS", "listOfTasks -> ${listOfTasks?.size}")
        listOfTasks?.let { list ->
            val listToDo = list.filter { task -> !task.checkState }.size
            val listDone = list.filter { task -> task.checkState }.size
            _taskToDo.postValue(listToDo)
            _taskDone.postValue(listDone)
        }
    }
}