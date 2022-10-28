/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.settings

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.domain.userdataUseCase.mapToSettingsModelDomain
import com.softyorch.taskapp.ui.models.SettingsModelUi
import com.softyorch.taskapp.ui.models.mapToSettingsModelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataUseCases: UserDataUseCases
) : ViewModel() {

    private val _settings = MutableLiveData<SettingsModelUi>()
    val settings: LiveData<SettingsModelUi> = _settings

    private val _manualThemeShow = MutableLiveData(false)
    val manualThemeShow: LiveData<Boolean> = _manualThemeShow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        loadUserData()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect {
                _manualThemeShow.postValue(!it.lightDarkAutomaticTheme)
                _settings.postValue(it)
                _isLoading.postValue(false)
            }
        }
    }

    fun applyChanges(settings: SettingsModelUi) {
        _isLoading.value = true
        updatePreferences(settingsModelUi = settings)
    }

    fun manualShow(show: Boolean) {
        _manualThemeShow.value = show
    }

    private fun updatePreferences(settingsModelUi: SettingsModelUi) {
        viewModelScope.launch {
            this.launch(Dispatchers.IO) {
                updateUser(settingsModelUi = settingsModelUi)
            }.join()
            _isLoading.value = false
        }
    }

    private fun getData() = userDataUseCases.getSettings().map {
        it.mapToSettingsModelUi()
    }

    private suspend fun updateUser(settingsModelUi: SettingsModelUi) =
        userDataUseCases.saveSettings(settingsModelUi.mapToSettingsModelDomain())

}

