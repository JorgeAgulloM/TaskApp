package com.softyorch.taskapp.presentation.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val datastore: DatastoreRepository
) : ViewModel() {

    private val _goToAutologin = MutableLiveData<Boolean>()
    val goToAutologin: LiveData<Boolean> = _goToAutologin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        loadData()
    }

    private fun getUser() = datastore.getData()

    private fun loadData() = userActivated()

    private fun userActivated() {
        viewModelScope.launch(Dispatchers.IO) {
            getUser().collect() { user ->
                if (user.rememberMe) {
                    logInWithRememberMe(
                        email = user.userEmail,
                        pass = user.userPass
                    ).data.let { userLogin ->
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

    private suspend fun logInWithRememberMe(email: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(email = email, password = pass)

    private fun isAutoLoginTime(user: UserData) {
        val timeWeekInMillis =
            timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
        user.lastLoginDate?.time?.let { autoLoginLimit ->
            val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
            timeWeekInMillis.compareTo(dif).let {
                when (it) {
                    1 -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            datastore.saveData(userData = user)
                        }
                        _goToAutologin.postValue(true)
                        _isLoading.postValue(false)
                    }
                    else -> {
                        _goToAutologin.postValue(false)
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }
}