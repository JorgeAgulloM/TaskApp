package com.softyorch.taskapp.ui.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {

    private val _settings = MutableLiveData<UserData>()
    val settings: LiveData<UserData> = _settings

    private val _reloading = MutableLiveData<Boolean>()
    val reloading: LiveData<Boolean> = _reloading

    init {
        _settings.postValue(getUserActiveSharedPreferences())
    }

    suspend fun applyChanges() {
        _settings.value?.let {
            _reloading.value = true
            updatePreferences(settingsUserData = it)
            delay(100)
            _reloading.value = false
        }
    }

    private fun getUserActiveSharedPreferences(): UserData? {
        return stateLogin.userDataActive
    }

    fun updatePreferences(settingsUserData: UserData) = viewModelScope.launch {
        stateLogin.userDataActive?.let { userData ->
            userData.lastLoginDate = settingsUserData.lastLoginDate
            userData.rememberMe = settingsUserData.rememberMe
            userData.lightDarkAutomaticTheme = settingsUserData.lightDarkAutomaticTheme
            userData.lightOrDarkTheme = settingsUserData.lightOrDarkTheme
            userData.automaticLanguage = settingsUserData.automaticLanguage
            userData.automaticColors = settingsUserData.automaticColors
            userData.timeLimitAutoLoading = settingsUserData.timeLimitAutoLoading
            userData.textSize = settingsUserData.textSize

            updateUser(userData = userData)
            stateLogIn(userData = userData)
        }
    }

    private suspend fun updateUser(userData: UserData) =
        repository.updateUserData(userData = userData)

    private fun stateLogIn(userData: UserData) = stateLogin.logIn(userData = userData)

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()
}

