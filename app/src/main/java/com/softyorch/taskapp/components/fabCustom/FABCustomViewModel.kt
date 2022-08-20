package com.softyorch.taskapp.components.fabCustom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.Task
import com.softyorch.taskapp.repository.TaskRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    fun getUserName(): String {
        val user = stateLogin.userDataActive?.username.toString()
        Log.d("FABVM", "user of autologin -> $user")
        Log.d("AUTOLOGINLOAD", "autologin FAB -> $stateLogin")
        return user
    }

    fun addTask(task: Task) = viewModelScope.launch { repository.addTask(task = task) }
}