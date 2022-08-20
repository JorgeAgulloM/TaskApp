package com.softyorch.taskapp.components.fabCustom

import android.util.Log
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.login.AutoLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FABCustomViewModel @Inject constructor(
    private val autoLogin: AutoLogin
) : ViewModel() {
    fun getUserName(): String {
        val user = autoLogin.userDataActive?.username.toString()
        Log.d("FABVM", "user of autologin -> $user")
        Log.d("AUTOLOGINLOAD", "autologin FAB -> $autoLogin")
        return user
    }
}