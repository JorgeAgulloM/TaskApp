package com.softyorch.taskapp.ui.screens.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserData
import com.softyorch.taskapp.data.repository.DatastoreRepository
import com.softyorch.taskapp.data.repository.UserDataRepository
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
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
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() = userActivated()

    private suspend fun userActivated() {
        datastore.getData().let { resource ->
            when (resource) {
                is Resource.Error -> {
                    Log.d(
                        "Resource",
                        "Resource.userActivated() -> error"
                    )
                }
                is Resource.Loading -> Log.d(
                    "Resource",
                    "Resource.userActivated() -> loading..."
                )
                is Resource.Success -> {
                    resource.data?.flowOn(Dispatchers.IO)?.collect { user ->
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
        }
    }

    private suspend fun logInWithRememberMe(email: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(email = email, password = pass)

    private suspend fun isAutoLoginTime(user: UserData) {
        val timeWeekInMillis =
            timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
        user.lastLoginDate?.time?.let { autoLoginLimit ->
            val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
            timeWeekInMillis.compareTo(dif).let {
                if (it == 1) {
                    datastore.saveData(userData = user)

                    _goToAutologin.value = true
                    _isLoading.value = false
                } else {
                    _goToAutologin.value = false
                    _isLoading.value = false
                }
            }
        }
    }
}
