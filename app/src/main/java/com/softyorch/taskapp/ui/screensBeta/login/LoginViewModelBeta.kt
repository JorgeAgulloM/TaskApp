package com.softyorch.taskapp.ui.screensBeta.login

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
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModelBeta @Inject constructor(
    private val pexelsUseCases: PexelsUseCases,
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), WithOutErrorsLogin, WithOutErrorsNewAccount, IsActivatedButton {

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

    private val _foundError = MutableLiveData<Boolean>()

    private val newAccountModelInterface = MutableLiveData(NewAccountModel.newAccountModel)
    val newAccountModel: LiveData<NewAccountModel> = newAccountModelInterface

    private val _errorsNewAccount = MutableLiveData(ErrorNewAccountModel.errorNewAccountModel)
    val errorsNewAccount: LiveData<ErrorNewAccountModel> = _errorsNewAccount

    private val foundErrorNewAccountInterface = MutableLiveData(false)

    init {
        _isLoading.value = true
        viewModelScope.launch {
            loadImage()
            autoLogin()
        }
    }

    fun hideNewAccount() {
        resetErrors()
        _showNewAccount.value = false
    }

    fun showNewAccount() {
        resetErrors()
        _showNewAccount.value = true
    }

    private fun loadImage() = viewModelScope.launch {
        pexelsUseCases.getImage.invoke().let { data ->
            data.mapToMediaModel().let { media ->
                _pexelsImage.value = media
                _showBody.postValue(true)
            }
        }
    }


    /** AutoLogin **/

    private suspend fun autoLogin() =
        getDataDs().let { data ->
            if (data != null) {
                data.flowOn(Dispatchers.IO).collect { user ->
                    if (user.rememberMe) {
                        signIn(user.mapToLoginModel())?.let { userLogin ->
                            if (isInTime(userLogin)) {
                                updateDatastore(userLogin)
                                delay(2000)
                                _autoLogin.postValue(true)
                                _isLoading.postValue(false)
                            } else _isLoading.postValue(false)
                        }
                    } else {
                        _isLoading.postValue(false)
                    }
                }
            } else {
                _isLoading.postValue(false)
            }
        }

    private fun isInTime(userDataEntity: UserDataEntity): Boolean {
        val timeWeekInMillis =
            timeLimitAutoLoginSelectTime(userDataEntity.timeLimitAutoLoading)
        userDataEntity.lastLoginDate?.time?.let { autoLoginLimit ->
            val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
            timeWeekInMillis.compareTo(dif).let { if (it == 1) return true }
        }
        return false
    }


    /** Login **/

    fun onLoginInputChange(loginModel: LoginModel) {
        _loginModel.value = loginModel
        if (_foundError.value == true) {
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
                    errors.email = !it
                    errors.pass = !it
                    errors.errorResultSignIn = !it
                    errors.error = !it
                    _errorsLogin.postValue(errors)
                    _foundError.postValue(false)

                    _isLoading.value = false
                    return errors.error
                }
            } else {
                _errorsLogin.postValue(errors)
                if (_foundError.value != true) _foundError.postValue(true)
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
            } else {
                _errorsLogin.postValue(
                    ErrorLoginModel(
                        errorResultSignIn = true,
                        error = true
                    )
                )
            }
        }
        return false
    }

    private fun resetErrors() {
        _errorsLogin.value = ErrorLoginModel.errorLoginModel
        _errorsNewAccount.value = ErrorNewAccountModel.errorNewAccountModel
    }

    /** New Account */

    fun onNewAccountInputChange(newAccountModel: NewAccountModel) {
        newAccountModelInterface.value = newAccountModel
        if (foundErrorNewAccountInterface.value == true) {
            setErrorsNewAccount(withOutErrorsNewAccount(newAccountModel))
        } else _errorsNewAccount.postValue(
            ErrorNewAccountModel(isActivatedButton = isActivatedButton(newAccountModel))
        )
    }

    private fun setErrorsNewAccount(errors: ErrorNewAccountModel) {
        if (foundErrorNewAccountInterface.value != true) foundErrorNewAccountInterface.postValue(
            true
        )
        _errorsNewAccount.postValue(errors)
    }

    suspend fun onNewAccountDataSend(
        newAccountModel: NewAccountModel
    ): Boolean {
        _isLoading.postValue(true)
        withOutErrorsNewAccount(newAccountModel).let { errors ->
            if (!errors.error) {
                addNewUser(
                    newAccountModel
                ).also { isError -> //When true returned, the user has been created else, this email exist now.
                    errors.apply {
                        emailExists = !isError
                        error = !isError
                    }
                    setErrorsNewAccount(errors)

                    _isLoading.postValue(false)
                    return errors.error
                }
            } else {
                setErrorsNewAccount(errors)

                _isLoading.postValue(false)
                return errors.error
            }
        }
    }

    /** Data **/

    private fun getDataDs() = datastore.getData.invoke()

    private suspend fun updateDatastore(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity)

    private suspend fun signIn(loginModel: LoginModel): UserDataEntity? =
        userDataUseCases.loginUser(loginModel.userEmail, loginModel.userPass)

    private suspend fun addNewUser(newAccountModel: NewAccountModel): Boolean =
        userDataUseCases.newAccountUser(newAccountModel.mapToUserDataEntity())

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity)

}
