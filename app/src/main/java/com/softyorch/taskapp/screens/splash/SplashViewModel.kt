package com.softyorch.taskapp.screens.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.login.AutoLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val autoLogin: AutoLogin
) : ViewModel() {
    suspend fun logInWithRememberMe(name: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(name = name, password = pass)

    fun loadSharedPreferencesInAutoLogin(sharedPreferences: SharedPreferences) {
        autoLogin.loadSharedPreferences(sharedPreferences = sharedPreferences)
    }

    suspend fun userActivated(): Boolean {
        if (autoLogin.isTheUserActive() == true) {
            autoLogin.userActive().let { userData ->
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
                                    autoLogin.logIn(userData = user)
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