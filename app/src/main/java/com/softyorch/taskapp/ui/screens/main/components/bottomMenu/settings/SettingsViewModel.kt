/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel() {

    private val _settings = MutableLiveData<UserDataEntity>()
    val settings: LiveData<UserDataEntity> = _settings

    private val _manualThemeShow = MutableLiveData(false)
    val manualThemeShow: LiveData<Boolean> = _manualThemeShow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect {
                    _manualThemeShow.postValue(!it.lightDarkAutomaticTheme)
                    _settings.postValue(it)
                    _isLoading.postValue(false)
                }
        }
    }

    fun applyChanges(settings: UserDataEntity) {
        _isLoading.value = true
        updatePreferences(userDataEntity = settings)
    }

    fun manualShow(show: Boolean) {
        _manualThemeShow.value = show
    }

    private fun updatePreferences(userDataEntity: UserDataEntity) {
        viewModelScope.launch {
            this.launch(Dispatchers.IO) {
                updateData(userDataEntity = userDataEntity)
            }.join()
            this.launch(Dispatchers.IO) {
                updateUser(userDataEntity = userDataEntity)
            }.join()
            _isLoading.value = false
        }
    }

    private fun getData() = datastore.getData()

    private suspend fun updateData(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity = userDataEntity)

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity = userDataEntity)

}

