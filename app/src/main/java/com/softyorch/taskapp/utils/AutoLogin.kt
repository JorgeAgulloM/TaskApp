package com.softyorch.taskapp.utils

import android.content.SharedPreferences
import com.softyorch.taskapp.model.UserData

class AutoLogin(
    private var sharedPreferences: SharedPreferences
) {

    fun logIn(
        name: String = "",
        pass: String = "",
        activate: Boolean = false,
        remember: Boolean = false
    ) {
        editSP(name = name, pass = pass, activate = activate, remember = remember)
    }

    fun logOut() {
        editSP()
    }

    private fun editSP(
        name: String = "",
        pass: String = "",
        activate: Boolean = false,
        remember: Boolean = false
    ) {
        sharedPreferences.edit().let { sp ->
            sp.putString("name", name)
            sp.putString("pass", pass)
            sp.putString("activate", activate.toString())
            sp.putString("remember", remember.toString())

            sp.apply()
        }
    }

    fun userActive(): UserData {
        sharedPreferences.let { sp ->
            return UserData(
                username = sp.getString("name", "").toString(),
                userEmail = "",
                userPass = sp.getString("pass", "").toString(),
                rememberMe = sp.getString("remember", "").toBoolean()
            )
        }
    }

    fun isTheUserActive(): Boolean? {
        userActive().let { user ->
            return user.rememberMe
        }
    }
}