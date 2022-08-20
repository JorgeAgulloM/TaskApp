package com.softyorch.taskapp.screens.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    suspend fun logInWithRememberMe(name: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(name = name, password = pass)

    fun loadSharedPreferencesInAutoLogin(sharedPreferences: SharedPreferences) {
        stateLogin.loadSharedPreferences(sharedPreferences = sharedPreferences)
    }

    suspend fun userActivated(): Boolean {
        if (stateLogin.isTheUserActive() == true) {
            stateLogin.userActive().let { userData ->
                logInWithRememberMe(
                    name = userData.username,
                    pass = userData.userPass
                ).let { data ->
                    data.data?.let { user ->
                        if (user.rememberMe == true) {
                            val timeWeekInMillis = 604800000
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