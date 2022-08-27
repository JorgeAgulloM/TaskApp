package com.softyorch.taskapp.ui.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
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

    init {
        viewModelScope.launch {
            _goToAutologin.value = userActivated()
        }
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
                                    stateLogin.logIn(userData = user)
                                    return true
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