package com.softyorch.taskapp.ui.components.fabCustom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.repository.TaskRepository
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _user = MutableLiveData<String>()
    val user: LiveData<String> = _user

    init {
        _user.postValue(getUserName())
    }

    private fun getUserName(): String {
        val user = stateLogin.userDataActive?.username.toString()
        Log.d("FABVM", "user of autologin -> $user")
        Log.d("AUTOLOGINLOAD", "autologin FAB -> $stateLogin")
        return user
    }

    fun addTask(task: Task) = viewModelScope.launch { repository.addTask(task = task) }

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()
}