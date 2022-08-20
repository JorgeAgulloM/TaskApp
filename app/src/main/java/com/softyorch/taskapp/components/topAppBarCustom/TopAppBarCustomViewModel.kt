package com.softyorch.taskapp.components.topAppBarCustom

import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopAppBarCustomViewModel @Inject constructor(
    private val stateLogin: StateLogin
) : ViewModel() {
    fun getUserPicture(): String? = stateLogin.userDataActive?.userPicture
}