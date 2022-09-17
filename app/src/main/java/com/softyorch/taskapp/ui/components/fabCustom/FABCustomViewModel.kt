package com.softyorch.taskapp.ui.components.fabCustom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.Task
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val datastore: DatastoreRepository
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

    fun addTask(task: Task) = viewModelScope.launch { repository.addTask(task = task) }
}