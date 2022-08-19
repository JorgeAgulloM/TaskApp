package com.softyorch.taskapp.utils.login

import android.content.SharedPreferences
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class AutoLogin @Inject constructor(
) {

    private var _sharedPreferences: SharedPreferences? = null
    var userDataActive: UserData? = null

    fun logIn(
        userData: UserData
    ) {
        userDataActive = userData
        sharedPreferencesSetUser(
            name = userData.username,
            pass = userData.userPass,
            activate = true,
            remember = userData.rememberMe == true
        )
    }

    fun logOut() {
        sharedPreferencesSetUser()
    }

    private fun sharedPreferencesSetUser(
        name: String = "",
        pass: String = "",
        activate: Boolean = false,
        remember: Boolean = false
    ) {
        _sharedPreferences?.edit().let { sp ->
            sp?.putString("name", name)
            sp?.putString("pass", pass)
            sp?.putString("activate", activate.toString())
            sp?.putString("remember", remember.toString())
        }
    }

    fun userActive(): UserData {
        _sharedPreferences.let { sp ->
            return UserData(
                username = sp?.getString("name", "").toString(),
                userEmail = "",
                userPass = sp?.getString("pass", "").toString(),
                rememberMe = sp?.getString("remember", "").toBoolean()
            )
        }
    }

    fun isTheUserActive(): Boolean? {
        userActive().let { user ->
            return user.rememberMe
        }
    }

    fun loadSharedPreferences(sharedPreferences: SharedPreferences) {
        _sharedPreferences = sharedPreferences
    }
}