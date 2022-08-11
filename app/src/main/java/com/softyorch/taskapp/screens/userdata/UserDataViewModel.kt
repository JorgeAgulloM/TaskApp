package com.softyorch.taskapp.screens.userdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val repository: UserDataRepository): ViewModel() {
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


















}