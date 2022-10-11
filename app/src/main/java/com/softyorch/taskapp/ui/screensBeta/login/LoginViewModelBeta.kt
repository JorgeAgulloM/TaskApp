package com.softyorch.taskapp.ui.screensBeta.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginViewModelBeta @Inject constructor() : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showLogin = MutableLiveData<Boolean>(false)
    val showLogin: LiveData<Boolean> = _showLogin

    private val _showNewAccount = MutableLiveData<Boolean>(false)
    val showNewAccount: LiveData<Boolean> = _showNewAccount

    private val _loginModel = MutableLiveData<LoginModel>()
    val loginModel: LiveData<LoginModel> = _loginModel

    fun showLogin() {
        _showLogin.value = true
    }

    fun showNewAccount() {
        _showNewAccount.value = true
    }


}
