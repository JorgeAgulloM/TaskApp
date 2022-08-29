package com.softyorch.taskapp.presentation.screens.userdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _userDataList = MutableStateFlow<List<UserData>>(emptyList())
    val userDataList = _userDataList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUser().distinctUntilChanged()
                .collect { listOfUserData ->
                    if (listOfUserData.isNullOrEmpty()) {
                        //TODO meter la barra de carga para mostrar al usuario
                    } else {
                        _userDataList.value = listOfUserData
                    }
                }
        }
    }

    suspend fun getUserDataId(id: String): Resource<UserData> = repository.getUserDataId(id = id)

    fun addUserData(userData: UserData) = viewModelScope.launch {
        repository.addUserData(userData = userData)
    }

    fun updateUserData(userData: UserData) = viewModelScope.launch {
        repository.updateUserData(userData = userData)
    }

    /*fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUsers()
    }*/
    fun deleteUserData(userData: UserData) = viewModelScope.launch {
        repository.deleteUserData(userData = userData)
    }

    fun logOut() = stateLogin.logOut()
}