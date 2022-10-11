package com.softyorch.taskapp.ui.screens.settings


import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel() {

    private val _settings = MutableLiveData<UserDataEntity>()
    val settings: LiveData<UserDataEntity> = _settings

    private val _reloading = MutableLiveData<Boolean>()
    val needReload: LiveData<Boolean> = _reloading

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //private val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val minSdk31 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    init {
        _isLoading.value = true
        loadUserData()
    }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getData().let { resource ->
            if (resource != null) {
                resource.flowOn(Dispatchers.IO).collect { data ->
                    userDataUseCases.getUserEmailExist(email = data.userEmail).let { user ->
                        var isNeedUpdate = false
                        if (!minSdk29) {
                            user?.lightDarkAutomaticTheme = false
                            if (!minSdk31) {
                                user?.automaticColors = false
                                isNeedUpdate = true
                            }
                        }
                        _settings.postValue(user)
                        if (isNeedUpdate) updatePreferences(userDataEntity = user!!)
                        _isLoading.postValue(false)
                    }
                }
            } else {
                //TODO
            }
        }
    }

    fun reloaded() {
        _reloading.value = false
        _isLoading.value = false
    }

    fun applyChanges() {
        _isLoading.value = true
        _settings.value?.let {
            updatePreferences(userDataEntity = it)
        }
    }

    private fun updatePreferences(userDataEntity: UserDataEntity) {
        viewModelScope.launch {
            this.launch(Dispatchers.IO) {
                updateUser(userDataEntity = userDataEntity)
            }.join()
            this.launch(Dispatchers.IO) {
                updateData(userDataEntity = userDataEntity)
            }.join()
            _reloading.value = true
        }
    }

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity = userDataEntity)

    private suspend fun updateData(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity = userDataEntity)

}

