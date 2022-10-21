package com.softyorch.taskapp.ui.components.fabCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.taskUsesCase.TaskModelUseCase
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.models.mapToTaskModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val taskUseCase: TaskUseCases
) : ViewModel() {
    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    init {
        getUserName()
    }

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect { user ->
                _user.postValue(user.username)
            }
        }
    }

    fun addTask(taskModelUi: TaskModelUi) =
        viewModelScope.launch(Dispatchers.IO) {
            addNewTask(taskModelUseCase = taskModelUi.mapToTaskModelUseCase())
        }

    /** Data */

    private fun getData() = datastore.getData()

    private suspend fun addNewTask(taskModelUseCase: TaskModelUseCase) =
        taskUseCase.addNewTask(taskModelUseCase)

}