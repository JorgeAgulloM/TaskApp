package com.softyorch.taskapp.presentation.components.topAppBarCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopAppBarCustomViewModel @Inject constructor(
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _imageUser = MutableLiveData<String>()
    val imageUser: LiveData<String> = _imageUser

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        _imageUser.value = getUserPicture()
        _userName.value = getUserName()
    }

    private fun getUserPicture(): String? = stateLogin.userDataActive?.userPicture
    private fun getUserName(): String? = stateLogin.userDataActive?.username
}