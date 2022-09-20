package com.softyorch.taskapp.ui.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.LoginUserUseCase
import com.softyorch.taskapp.domain.userdataUseCase.NewAccountUserUseCase
import com.softyorch.taskapp.domain.userdataUseCase.UpdateUserUseCase
import com.softyorch.taskapp.ui.errors.ErrorInterface
import com.softyorch.taskapp.ui.errors.ErrorUserInput
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val loginUserUseCase: LoginUserUseCase,
    private val newAccountUserUseCase: NewAccountUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel(), ErrorInterface {
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

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorEmail = MutableLiveData<Boolean>()
    val errorEmail: LiveData<Boolean> = _errorEmail

    private val _errorRepeatEmail = MutableLiveData<Boolean>()
    val errorRepeatEmail: LiveData<Boolean> = _errorRepeatEmail

    private val _errorEmailExists = MutableLiveData<Boolean>()
    val errorEmailExists: LiveData<Boolean> = _errorEmailExists

    private val _errorPass = MutableLiveData<Boolean>()
    val errorPass: LiveData<Boolean> = _errorPass

    private val _errorRepeatPass = MutableLiveData<Boolean>()
    val errorRepeatPass: LiveData<Boolean> = _errorRepeatPass

    private val _errorEmailOrPassIncorrect = MutableLiveData<Boolean>()
    val errorEmailOrPassIncorrect: LiveData<Boolean> = _errorEmailOrPassIncorrect

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _foundError = MutableLiveData<Boolean>()

    fun onLoginInputChange(email: String, pass: String, rememberMe: Boolean) {
        _email.value = email
        _pass.value = pass
        _rememberMe.value = rememberMe
        _loginEnable.value = true
        if (_foundError.value == true) {
            setErrorsLogin(error = withOutErrors(email = email, pass = pass))
        }
    }

    suspend fun onLoginDataSend(
        email: String,
        pass: String
    ): Boolean {
        _isLoading.value = true
        withOutErrors(
            email = email,
            pass = pass
        ).let { error ->
            if (!error.error) {
                loginAndUpdateData(
                    email = email, password = pass, rememberMe = rememberMe.value!!
                ).also {
                    error.emailOrPassIncorrect = !it
                    error.email = !it
                    error.pass = !it
                    error.error = !it
                    setErrorsLogin(error = error)

                    _isLoading.value = false
                    return error.error
                }
            } else {
                setErrorsLogin(error = error)

                _isLoading.value = false
                return error.error
            }
        }
    }

    private fun setErrorsLogin(error: ErrorUserInput.Error) {
        if (_foundError.value != true) _foundError.postValue(true)
        _errorEmail.value = error.email
        _errorPass.value = error.pass
        _errorEmailOrPassIncorrect.value = error.emailOrPassIncorrect
        _error.value = error.error
    }

    fun onNewAccountInputChange(
        name: String,
        email: String,
        emailRepeat: String,
        pass: String,
        passRepeat: String,
        image: String = emptyString
    ) {
        _name.value = name
        _email.value = email
        _emailRepeat.value = emailRepeat
        _pass.value = pass
        _passRepeat.value = passRepeat
        _image.value = image
        _newAccountEnable.value = true
        if (_foundError.value == true) {
            setErrorsNewAccount(
                error = withOutErrors(
                    name = name,
                    email = email,
                    emailRepeat = emailRepeat,
                    pass = pass,
                    passRepeat = passRepeat
                )
            )
        }
    }

    suspend fun onNewAccountDataSend(
        name: String,
        email: String,
        emailRepeat: String,
        pass: String,
        passRepeat: String
    ): Boolean {
        _isLoading.value = true
        withOutErrors(
            name = name,
            email = email,
            emailRepeat = emailRepeat,
            pass = pass,
            passRepeat = passRepeat
        ).let { error ->
            if (!error.error) {
                addNewUser(
                    UserDataEntity(
                        username = name,
                        userEmail = email,
                        userPass = pass
                    )
                ).also {
                    error.emailExists = !it
                    error.email = !it
                    error.error = !it
                    setErrorsNewAccount(error = error)

                    _isLoading.value = false
                    return error.error
                }
            } else {
                setErrorsNewAccount(error = error)

                _isLoading.value = false
                return error.error
            }
        }
    }

    private fun setErrorsNewAccount(error: ErrorUserInput.Error) {
        if (_foundError.value != true) _foundError.postValue(true)
        _errorName.value = error.name
        _errorEmail.value = error.email
        _errorRepeatEmail.value = error.emailRepeat
        _errorPass.value = error.pass
        _errorRepeatPass.value = error.passRepeat
        _errorEmailExists.value = error.emailExists
        _error.value = error.error
    }

    private suspend fun loginAndUpdateData(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Boolean {
        signInUserWithEmailAndPassword(email = email, password = password).let { user ->
            if (user != null) {
                user.lastLoginDate = Date.from(Instant.now())
                user.rememberMe = rememberMe
                updateLastLoginUser(userDataEntity = user)
                datastore.saveData(userDataEntity = user)
                return true
            } else {
                _error.postValue(true)
                _errorEmailOrPassIncorrect.postValue(true)
            }
        }
        return false
    }

    fun resetErrorChangeLoginToNewAccountVis() {
        _errorName.value = false
        _errorEmail.value = false
        _errorRepeatEmail.value = false
        _errorPass.value = false
        _errorRepeatPass.value = false
        _error.value = false
        _errorEmailExists.value = false
        _errorEmailOrPassIncorrect.value = false
        _foundError.value = false
    }

    /**
     * data
     */

    private suspend fun addNewUser(userDataEntity: UserDataEntity): Boolean {
        return try {
            newAccountUserUseCase(userDataEntity = userDataEntity)
            true
        } catch (ex: Exception) {
            false
        }
    }

    private fun updateLastLoginUser(userDataEntity: UserDataEntity) =
        viewModelScope.launch { updateUserUseCase(userDataEntity = userDataEntity) }

    private suspend fun signInUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserDataEntity? = loginUserUseCase(email = email, password = password)

}

