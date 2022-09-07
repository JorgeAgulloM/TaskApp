package com.softyorch.taskapp.presentation.screens.settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.datastore.DatastoreDataBase
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin,
    private val datastore: DatastoreRepository
) : ViewModel() {

    private val _settings = MutableLiveData<UserData>()
    val settings: LiveData<UserData> = _settings

    private val _reloading = MutableLiveData<Boolean>()
    val reloading: LiveData<Boolean> = _reloading

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        //_settings.postValue(getUserActiveSharedPreferences())
        viewModelScope.launch(Dispatchers.IO) { getUserData() }.join()
        _isLoading.value = false
    }

    suspend fun applyChanges() {
        _isLoading.value = true
        _settings.value?.let {
            val result = updatePreferences(userData = it)
            result.join()

            _isLoading.postValue(false)
        }
    }

    private suspend fun getUserData() = datastore.getData().collect { userData ->
        _settings.postValue(userData)
    }


    /*private fun getUserActiveSharedPreferences(): UserData? {
        return stateLogin.userDataActive
    }*/

    private fun updatePreferences(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {
        /*stateLogin.userDataActive?.let { userData ->
            userData.lastLoginDate = settingsUserData.lastLoginDate
            userData.rememberMe = settingsUserData.rememberMe
            userData.lightDarkAutomaticTheme = settingsUserData.lightDarkAutomaticTheme
            userData.lightOrDarkTheme = settingsUserData.lightOrDarkTheme
            userData.automaticLanguage = settingsUserData.automaticLanguage
            userData.automaticColors = settingsUserData.automaticColors
            userData.timeLimitAutoLoading = settingsUserData.timeLimitAutoLoading
            userData.textSize = settingsUserData.textSize
            //stateLogIn(userData = userData)
        }*/
        updateUser(userData = userData)
        updateData(userData = userData)
    }

    private suspend fun updateUser(userData: UserData) = repository.updateUserData(userData = userData)
    private suspend fun updateData(userData: UserData) = datastore.saveData(userData = userData)

    //private fun stateLogIn(userData: UserData) = stateLogin.logIn(userData = userData)
}

