package com.softyorch.taskapp.utils

import android.content.SharedPreferences
import android.util.Log
import com.softyorch.taskapp.model.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateLogin @Inject constructor(
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
        Log.d("AUTOLOGIN", "Log In & Load userDataActive -> $userDataActive")
    }

    fun logOut() {
        sharedPreferencesSetUser()
        Log.d("AUTOLOGIN", "Log Out")
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

            sp?.apply()
        }
    }

    fun userActive(): UserData {
        _sharedPreferences.let { sp ->
            Log.d("AUTOLOGIN", "User Activate on haredPreferences -> $sp")
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
            Log.d("AUTOLOGIN", "User Activate? -> $user")
            return user.rememberMe
        }
    }

    fun loadSharedPreferences(sharedPreferences: SharedPreferences) {
        _sharedPreferences = sharedPreferences
        Log.d("AUTOLOGIN", "SharedPreferences Load -> $_sharedPreferences")
    }
}