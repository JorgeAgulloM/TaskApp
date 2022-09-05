package com.softyorch.taskapp.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {

    private val _goToAutologin = MutableLiveData<Boolean>()
    val goToAutologin: LiveData<Boolean> = _goToAutologin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() {
        val result = viewModelScope.launch {
            _goToAutologin.value = userActivated()
        }
        result.join()
        _isLoading.postValue(false)
    }

    private suspend fun logInWithRememberMe(email: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(email = email, password = pass)

    private suspend fun userActivated(): Boolean {
        if (stateLogin.isTheUserActive()) {
            stateLogin.userActive().let { userData ->
                logInWithRememberMe(
                    email = userData.userEmail,
                    pass = userData.userPass
                ).let { data ->
                    data.data?.let { user ->
                        if (user.rememberMe) {
                            val timeWeekInMillis =
                                timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
                            user.lastLoginDate?.time?.let { timeDiff ->
                                Date.from(Instant.now()).time.minus(timeDiff)
                                timeDiff.compareTo(timeWeekInMillis).let {
                                    when (it) {
                                        -1 -> {
                                            stateLogin.logIn(userData = user)
                                            return true
                                        }
                                        1 -> {
                                            return false
                                        }
                                        else -> {
                                            return false
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}