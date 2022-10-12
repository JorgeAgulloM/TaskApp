package com.softyorch.taskapp.ui.screensBeta.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.pexelUseCase.PexelsUseCases
import com.softyorch.taskapp.ui.screens.splash.mapToMediaModelVM
import com.softyorch.taskapp.ui.screensBeta.login.model.LoginModel
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.mapToMediaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class LoginViewModelBeta @Inject constructor(
    private val pexelsUseCases: PexelsUseCases
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showLogin = MutableLiveData<Boolean>(false)
    val showLogin: LiveData<Boolean> = _showLogin

    private val _showNewAccount = MutableLiveData<Boolean>(false)
    val showNewAccount: LiveData<Boolean> = _showNewAccount

    private val _loginModel = MutableLiveData<LoginModel>()
    val loginModel: LiveData<LoginModel> = _loginModel

    private val _newAccountModel = MutableLiveData<NewAccountModel>()
    val newAccountModel: LiveData<NewAccountModel> = _newAccountModel

    private val _pexelsImage = MutableLiveData<MediaModel>(MediaModel.MediaModelEmpty)
    val pexelsImage: LiveData<MediaModel> = _pexelsImage


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
}
