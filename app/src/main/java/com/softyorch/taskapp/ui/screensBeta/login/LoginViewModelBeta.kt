package com.softyorch.taskapp.ui.screensBeta.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.database.userdata.mapToUserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.pexelUseCase.PexelsUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.screensBeta.login.errors.IsActivatedButton
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsLogin
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsNewAccount
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModelBeta @Inject constructor(
    private val pexelsUseCases: PexelsUseCases,
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), AutoLogin, WithOutErrorsLogin, WithOutErrorsNewAccount, IsActivatedButton {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showBody = MutableLiveData(false)
    val showBody: LiveData<Boolean> = _showBody

    private val _autoLogin = MutableLiveData(false)
    val autologin: LiveData<Boolean> = _autoLogin

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _showNewAccount = MutableLiveData<Boolean>()
    val showNewAccount: LiveData<Boolean> = _showNewAccount

    private val _loginModel = MutableLiveData<LoginModel>()
    val loginModel: LiveData<LoginModel> = _loginModel

    private val _pexelsImage = MutableLiveData(MediaModel.MediaModelEmpty)
    val pexelsImage: LiveData<MediaModel> = _pexelsImage

    private val _errorsLogin = MutableLiveData<ErrorLoginModel>()
    val errorsLogin: LiveData<ErrorLoginModel> = _errorsLogin

    private val _foundInputError = MutableLiveData<Boolean>()

    private val _newAccountModelInterface = MutableLiveData(NewAccountModel.newAccountModel)
    val newAccountModel: LiveData<NewAccountModel> = _newAccountModelInterface

    private val _errorsNewAccount = MutableLiveData(ErrorNewAccountModel.errorNewAccountModel)
    val errorsNewAccount: LiveData<ErrorNewAccountModel> = _errorsNewAccount

    private val foundErrorNewAccountInterface = MutableLiveData(false)

    init {
        _isLoading.value = true
        viewModelScope.launch {
            loadImage()
            _autoLogin.value = autologin(datastore, userDataUseCases::loginUser)
            _isLoading.value =false
        }
    }

    fun hideNewAccount() {
        resetErrors()
        _showNewAccount.postValue(false)
    }

    fun showNewAccount() {
        resetErrors()
        _showNewAccount.postValue(true)
    }

    private fun loadImage() {
        viewModelScope.launch {
            getImage().let { data ->
                data.mapToMediaModel().let { media ->
                    _pexelsImage.value = media
                    _showBody.postValue(true)
                }
            }
        }
    }

    /** Login **/

    fun onLoginInputChange(loginModel: LoginModel) {
        _loginModel.value = loginModel
        if (_foundInputError.value == true) {
            _errorsLogin.postValue(withOutErrorsLogin(loginModel))
        } else _errorsLogin.postValue(
            ErrorLoginModel(isActivatedButton = isActivatedButton(loginModel))
        )
    }

    suspend fun onLoginDataSend(loginModel: LoginModel): Boolean {
        _isLoading.value = true
        withOutErrorsLogin(loginModel).let { errors ->
            if (!errors.error) {
                loginAndUpdateData(loginModel).also {
                    errors.errorResultSignIn = !it
                    errors.error = !it
                    _errorsLogin.postValue(errors)
                    _foundInputError.postValue(false)
                    delay(500)
                    _isLoading.value = false
                    return errors.error
                }
            } else {
                _errorsLogin.postValue(errors)
                if (_foundInputError.value != true) _foundInputError.postValue(true)
                _isLoading.value = false
                return errors.error
            }
        }
    }

    private suspend fun loginAndUpdateData(loginModel: LoginModel): Boolean {
        signIn(loginModel).also { user ->
            if (user != null) {
                user.lastLoginDate = Date.from(Instant.now())
                user.rememberMe = loginModel.rememberMe
                _loginSuccess.postValue(true)
                updateUser(user)
                updateDatastore(user)
                return true
            }
        }
        return false
    }

    private fun resetErrors() {
        _errorsLogin.postValue(ErrorLoginModel.errorLoginModel)
        _errorsNewAccount.postValue(ErrorNewAccountModel.errorNewAccountModel)
    }

    /** New Account */

    fun onNewAccountInputChange(newAccountModel: NewAccountModel) {
        _newAccountModelInterface.value = newAccountModel
        if (foundErrorNewAccountInterface.value == true) {
            _errorsNewAccount.postValue(withOutErrorsNewAccount(newAccountModel))
        } else _errorsNewAccount.postValue(
            ErrorNewAccountModel(isActivatedButton = isActivatedButton(newAccountModel))
        )
    }

    suspend fun onNewAccountDataSend(
        newAccountModel: NewAccountModel
    ): Boolean {
        _isLoading.postValue(true)
        withOutErrorsNewAccount(newAccountModel).let { errors ->
            if (!errors.error) {
                addNewUser(newAccountModel).also { isError -> //When true returned, the user has been created else, this email exist now.
                    errors.emailExists = !isError
                    errors.error = !isError
                    _errorsNewAccount.postValue(errors)
                    Log.d("ERRORS","errors -> $errors")
                    delay(500)
                    _isLoading.postValue(false)
                    return errors.error
                }
            } else {
                _errorsNewAccount.postValue(errors)
                if (foundErrorNewAccountInterface.value != true)
                    foundErrorNewAccountInterface.postValue(true)
                _isLoading.postValue(false)
                return errors.error
            }
        }
    }

    /** Data **/

    private suspend fun updateDatastore(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity)

    private suspend fun signIn(loginModel: LoginModel): UserDataEntity? =
        userDataUseCases.loginUser(loginModel.userEmail, loginModel.userPass)

    private suspend fun addNewUser(newAccountModel: NewAccountModel): Boolean =
        userDataUseCases.newAccountUser(newAccountModel.mapToUserDataEntity())

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity)

    private suspend fun getImage() = pexelsUseCases.getImage()

}
