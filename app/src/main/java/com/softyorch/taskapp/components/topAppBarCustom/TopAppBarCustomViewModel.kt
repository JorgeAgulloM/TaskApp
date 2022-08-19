package com.softyorch.taskapp.components.topAppBarCustom

import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.login.AutoLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopAppBarCustomViewModel @Inject constructor(
    private val autoLogin: AutoLogin
) : ViewModel() {
    fun getUserPicture(): String? = autoLogin.userDataActive?.userPicture
}