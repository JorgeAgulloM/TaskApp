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
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsLogin
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsNewAccount
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorLoginModel
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModelBeta @Inject constructor(
    private val pexelsUseCases: PexelsUseCases,
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), WithOutErrorsLogin, WithOutErrorsNewAccount {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showLogin = MutableLiveData(false)
    val showLogin: LiveData<Boolean> = _showLogin

    private val _showNewAccount = MutableLiveData(false)
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

    private val errorsNewAccountInterface = MutableLiveData(ErrorNewAccountModel.errorNewAccountModel)
    val errorsNewAccount: LiveData<ErrorNewAccountModel> = errorsNewAccountInterface

    private val isLoadingNewAccountInterface = MutableLiveData(false)
    val isLoadingNewAccount: LiveData<Boolean> = isLoadingNewAccountInterface

    private val foundErrorNewAccountInterface = MutableLiveData(false)

    init {
        loadImage()
    }

    fun showLogin() {
        _showLogin.value = true
    }

    fun showNewAccount() {
        _showNewAccount.value = !showNewAccount.value!!
    }

    private fun loadImage() = viewModelScope.launch {
        pexelsUseCases.getImage.invoke().let { data ->
            data.mapToMediaModel().let { media ->
                _pexelsImage.value = media
                _isLoading.postValue(false)
            }
        }
    }

    /** Login **/

    fun onLoginInputChange(loginModel: LoginModel){
        _loginModel.value = loginModel
        if (_foundError.value == true) {
            _errorsLogin.postValue(withOutErrorsLogin(loginModel))
        }
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

    /** New Account */

    fun onNewAccountInputChange(newAccountModel: NewAccountModel) {
        newAccountModelInterface.value = newAccountModel
        if (foundErrorNewAccountInterface.value == true) {
            setErrorsNewAccount(withOutErrorsNewAccount(newAccountModel))
        }
    }

    private fun setErrorsNewAccount(errors: ErrorNewAccountModel) {
        if (foundErrorNewAccountInterface.value != true) foundErrorNewAccountInterface.postValue(true)
        errorsNewAccountInterface.postValue(errors)
    }

    suspend fun onNewAccountDataSend(
        newAccountModel: NewAccountModel
    ): Boolean {
        isLoadingNewAccountInterface.postValue(true)
        withOutErrorsNewAccount(newAccountModel).let { errors ->
            if (!errors.error) {
                addNewUser(
                    newAccountModel
                ).also { isError ->
                    errors.apply {
                        name = !isError
                        email = !isError
                        emailRepeat = !isError
                        emailExists = !isError
                        pass = !isError
                        passRepeat = !isError
                        error = !isError
                    }
                    setErrorsNewAccount(errors)

                    isLoadingNewAccountInterface.postValue(false)
                    return errors.error
                }
            } else {
                setErrorsNewAccount(errors)

                isLoadingNewAccountInterface.postValue(false)
                return errors.error
            }
        }
    }

    /** Data */

    private suspend fun signIn(loginModel: LoginModel): UserDataEntity? =
        userDataUseCases.loginUser(loginModel.userEmail, loginModel.userPass)

    private suspend fun updateDatastore(userDataEntity: UserDataEntity) {
        datastore.saveData(userDataEntity)
    }

    private suspend fun addNewUser(newAccountModel: NewAccountModel): Boolean  {
        return try {
            userDataUseCases.newAccountUser(newAccountModel.mapToUserDataEntity())
            true
        } catch (ex: Exception) {
            false
        }
    }

}
