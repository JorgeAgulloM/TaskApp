package com.softyorch.taskapp.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {

    fun getUserActiveSharedPreferences(): UserData? {
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

            repository.updateUserData(userData = userData)

            stateLogin.logIn(userData = userData)
        }
    }
}

