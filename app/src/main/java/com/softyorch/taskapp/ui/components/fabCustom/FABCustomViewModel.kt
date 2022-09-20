package com.softyorch.taskapp.ui.components.fabCustom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.tasks.TaskEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.taskUsesCase.AddNewTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val addNewTaskUseCase: AddNewTaskUseCase
) : ViewModel() {
    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    init {
        getUserName()
    }

    private fun getUserName() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData().let { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _user.postValue("Error")
                    }
                    is Resource.Loading -> Log.d(
                        "Resource",
                        "Resource.getUserName() -> loading..."
                    )
                    is Resource.Success -> {
                        resource.data?.flowOn(Dispatchers.IO)?.collect { user ->
                            _user.postValue(user.username)
                        }
                    }
                }
            }
        }
    }

    fun addTask(taskEntity: TaskEntity) =
        viewModelScope.launch(Dispatchers.IO) { addNewTaskUseCase(taskEntity = taskEntity) }
}