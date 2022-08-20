package com.softyorch.taskapp.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _userDataList = MutableStateFlow<List<UserData>>(emptyList())
    val userDataList = _userDataList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUser().distinctUntilChanged()
                .collect { listOfUsers ->
                    if (listOfUsers.isEmpty()) {
                        //TODO
                    } else _userDataList.value = listOfUsers
                }
        }
    }

    suspend fun signInUserWithNameAndPassword(name: String, password: String): Resource<UserData> =
        repository.signInUserWithNameAndPassword(name = name, password = password)

    fun addUser(userData: UserData) =
        viewModelScope.launch { repository.addUserData(userData = userData) }

    fun updateLastLoginUser(userData: UserData) =
        viewModelScope.launch { repository.updateUserData(userData = userData) }

    suspend fun loginUserIntent(name: String, password: String, rememberMe: Boolean): Boolean {
        signInUserWithNameAndPassword(name = name, password = password).let { data ->
            data.data?.let { user ->
                user.lastLoginDate = Date.from(Instant.now())
                user.rememberMe = rememberMe
                updateLastLoginUser(userData = user)
                stateLogin.logIn(userData = user)
                return true
            }
        }
        return false
    }
}