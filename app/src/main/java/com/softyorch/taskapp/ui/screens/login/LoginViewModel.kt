package com.softyorch.taskapp.ui.screens.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _userDataList = MutableStateFlow<List<UserData>>(emptyList())
    val userDataList = _userDataList.asStateFlow()

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

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _newAccountEnable = MutableLiveData<Boolean>()
    val newAccountEnable: LiveData<Boolean> = _newAccountEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onLoginChange(email: String, pass: String) {
        _email.value = email
        _pass.value = pass
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

    suspend fun onLoginSelected() {
        _isLoading.value = true
        //TODO
        _isLoading.value = false
    }

    suspend fun onNewAccountSelected() {
        _isLoading.value = true
        //TODO
        _isLoading.value = false
    }

    private fun isNameValid(name: String): Boolean = name.length >= 3

    private fun isValidPass(pass: String): Boolean = pass.length >= 8

    private fun isValidPass(pass: String, passRepeat: String): Boolean =
        pass.length >= 8 && passRepeat.length >= 8

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidEmail(email: String, emailRepeat: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                Patterns.EMAIL_ADDRESS.matcher(emailRepeat).matches()
    }


    /**
     * stateLogin
     */

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()

    /**
     * data
     */

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUser().distinctUntilChanged()
                .collect { listOfUsers ->
                    if (listOfUsers.isEmpty()) {
                        //TODO
                    } else _userDataList.value = listOfUsers
                }
        }
    }

    private suspend fun signInUserWithNameAndPassword(
        name: String,
        password: String
    ): Resource<UserData> =
        repository.signInUserWithNameAndPassword(name = name, password = password)

    fun addUser(userData: UserData) =
        viewModelScope.launch { repository.addUserData(userData = userData) }

    private fun updateLastLoginUser(userData: UserData) =
        viewModelScope.launch { repository.updateUserData(userData = userData) }

    suspend fun loginUserIntent(name: String, password: String, rememberMe: Boolean): Boolean {
        signInUserWithNameAndPassword(name = name, password = password).let { data ->
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
}

