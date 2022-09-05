package com.softyorch.taskapp.presentation.screens.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _emailRepeat = MutableLiveData<String>()
    val emailRepeat: LiveData<String> = _emailRepeat

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String> = _pass

    private val _passRepeat = MutableLiveData<String>()
    val passRepeat: LiveData<String> = _passRepeat

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    private val _rememberMe = MutableLiveData<Boolean>()
    val rememberMe: LiveData<Boolean> = _rememberMe

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _newAccountEnable = MutableLiveData<Boolean>()
    val newAccountEnable: LiveData<Boolean> = _newAccountEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onLoginChange(email: String, pass: String, rememberMe: Boolean) {
        _email.value = email
        _pass.value = pass
        _rememberMe.value = rememberMe
        _loginEnable.value = isValidEmail(email = email) && isValidPass(pass = pass)
    }

    fun onNewAccountChange(
        name: String,
        email: String,
        emailRepeat: String,
        pass: String,
        passRepeat: String,
        image: String = ""
    ) {
        _name.value = name
        _email.value = email
        _emailRepeat.value = emailRepeat
        _pass.value = pass
        _passRepeat.value = passRepeat
        _image.value = image
        _newAccountEnable.value = isNameValid(name = name) &&
                isValidEmail(email = email, emailRepeat = emailRepeat) &&
                isValidPass(pass = pass, passRepeat = passRepeat)
    }

    suspend fun onLoginSelected() : Boolean {
        _isLoading.value = true
        loginAndUpdateData(
            email = email.value!!, password = pass.value!!, rememberMe = rememberMe.value!!
        ).let {
            //delay(3000)
            _isLoading.value = false
            return it
        }
    }

    suspend fun onNewAccountSelected(): Boolean {
        _isLoading.value = true
        addNewUser(
            UserData(username = name.value!!, userEmail = email.value!!, userPass = pass.value!!)
        ).let {
            //delay(1000)
            _isLoading.value = false
            return it
        }
    }

    private fun isNameValid(name: String): Boolean = name.length >= 3

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidEmail(email: String, emailRepeat: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                Patterns.EMAIL_ADDRESS.matcher(emailRepeat).matches()
    }

    private fun isValidPass(pass: String): Boolean = pass.length >= 8

    private fun isValidPass(pass: String, passRepeat: String): Boolean =
        Pattern.matches(REGEX_PASSWORD, pass) &&
        pass == passRepeat


    /**
     * data
     */

    private suspend fun addNewUser(userData: UserData): Boolean {
        return repository.addUserData(userData = userData)
    }

    private fun updateLastLoginUser(userData: UserData) =
        viewModelScope.launch { repository.updateUserData(userData = userData) }

    private suspend fun loginAndUpdateData(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Boolean {
        signInUserWithEmailAndPassword(email = email, password = password).let { data ->
            data.data?.let { user ->
                user.lastLoginDate = Date.from(Instant.now())
                user.rememberMe = rememberMe
                updateLastLoginUser(userData = user)
                stateLogin.logIn(userData = user)
                return true
            }
        }
        return false
    }

    private suspend fun signInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<UserData> =
        repository.signInUserWithEmailAndPassword(email = email, password = password)


    /**
     * stateLogin
     */

}

