package com.softyorch.taskapp.ui.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.pexelUseCase.PexelsUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userDataUseCases: UserDataUseCases,
    private val datastore: DatastoreUseCases,
    private val pexelsUseCases: PexelsUseCases
) : ViewModel() {

    private val _goToAutologin = MutableLiveData<Boolean>()
    val goToAutologin: LiveData<Boolean> = _goToAutologin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getImage = MutableLiveData<String>()
    val getImage: LiveData<String> = _getImage

    private val _getUrl = MutableLiveData<String>()
    val getUrl: LiveData<String> = _getUrl

    private val _getAuthor = MutableLiveData<String>()
    val getAuthor: LiveData<String> = _getAuthor

    private val _getUrlAuthor = MutableLiveData<String>()
    private val _isError = MutableLiveData<Boolean>()

    val isError: LiveData<Boolean> = _isError
    val getUrlAuthor: LiveData<String> = _getUrlAuthor

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        _isLoading.value = true
        viewModelScope.launch {
            loadImage()
            loadData()
        }
    }

    private suspend fun loadData() = userActivated()

    private suspend fun loadImage() = viewModelScope.launch {
        pexelsUseCases.getImage.invoke().let { data ->
            data.mapToMediaModelVM().let { media ->
                _getImage.value = media.image
                _getUrl.value = media.imageUrl
                _getAuthor.value = media.author
                _getUrlAuthor.value = media.authorUrl
                _isLoading.postValue(false)
            }
        }
    }

    private fun userActivated() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData().collect { user ->
                if (user.rememberMe) {
                    logInWithRememberMe(
                        email = user.userEmail,
                        pass = user.userPass
                    ).let { userLogin ->
                        if (userLogin != null) {
                            isAutoLoginTime(user = userLogin)

                        } else {
                            _goToAutologin.postValue(false)
                            _isLoading.postValue(false)
                        }
                    }
                } else {
                    _goToAutologin.postValue(false)
                    _isLoading.postValue(false)
                }
            }
        }
    }

    private suspend fun logInWithRememberMe(email: String, pass: String): UserDataEntity? =
        userDataUseCases.loginUser(email = email, password = pass)

    private suspend fun isAutoLoginTime(user: UserDataEntity) {
        val timeWeekInMillis =
            timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
        user.lastLoginDate?.time?.let { autoLoginLimit ->
            val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
            timeWeekInMillis.compareTo(dif).let {
                if (it == 1) {
                    datastore.saveData(userDataEntity = user)

                    _goToAutologin.value = true
                    _isLoading.value = false
                } else {
                    _goToAutologin.value = false
                    _isLoading.value = false
                }
            }
        }
    }

    fun isShowError() {
        _isError.value = false
    }
}
