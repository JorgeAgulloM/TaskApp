package com.softyorch.taskapp.presentation.screens.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.data.data.datastore.DatastoreDataBase
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import com.softyorch.taskapp.utils.timeLimitAutoLoginSelectTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin,
    private val datastore: DatastoreDataBase
) : ViewModel() {

    private val _goToAutologin = MutableLiveData<Boolean>()
    val goToAutologin: LiveData<Boolean> = _goToAutologin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        viewModelScope.launch {
            loadData()
            pruebaDatastore()
        }
    }

    private suspend fun pruebaDatastore() {
        val prueba = try {
          viewModelScope.launch(Dispatchers.IO) {
              datastore.getData().collect(){

                  withContext(Dispatchers.Main){
                      /** Aquí se puede añadir un set a los datos de vista de UI*/
                  }
                  Log.d("DATASTORE", "it -> $it")
              }
          }

        } catch ( e: Exception) {

        }
        Log.d("DATASTORE", "prueba -> $prueba")
    }




    private suspend fun loadData() {
        val result = viewModelScope.launch {
            _goToAutologin.value = userActivated()
        }
        result.join()
        _isLoading.postValue(false)
    }

    private suspend fun logInWithRememberMe(email: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(email = email, password = pass)

    private suspend fun userActivated(): Boolean {
        if (stateLogin.isTheUserActive()) {
            stateLogin.userActive().let { userData ->
                logInWithRememberMe(
                    email = userData.userEmail,
                    pass = userData.userPass
                ).data?.let { user ->
                    return isAutoLogin(user = user)
                }
            }
        }
        return false
    }

    private fun isAutoLogin(user: UserData): Boolean {
        if (user.rememberMe) {
            val timeWeekInMillis =
                timeLimitAutoLoginSelectTime(user.timeLimitAutoLoading)
            user.lastLoginDate?.time?.let { autoLoginLimit ->
                val dif = Date.from(Instant.now()).time.minus(autoLoginLimit)
                timeWeekInMillis.compareTo(dif).let {
                    when (it) {
                        1 -> {
                            stateLogin.logIn(userData = user)
                            return true
                        }
                        else -> return false
                    }
                }
            }
        }
        return false
    }
}