package com.softyorch.taskapp.ui.components.fabCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.taskUsesCase.TaskUseCases
import com.softyorch.taskapp.ui.models.TaskModelUi
import com.softyorch.taskapp.ui.models.mapToTaskModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
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
            datastore.getData().let { resource ->
                if (resource != null) {
                    resource.flowOn(Dispatchers.IO).collect { user ->
                        _user.postValue(user.username)
                    }
                } else {
                    _user.postValue("Error")
                }
            }
        }
    }

    fun addTask(taskModelUi: TaskModelUi) =
        viewModelScope.launch(Dispatchers.IO) {
            taskUseCase.addNewTask(taskModelUseCase = taskModelUi.mapToTaskModelUseCase())
        }
}