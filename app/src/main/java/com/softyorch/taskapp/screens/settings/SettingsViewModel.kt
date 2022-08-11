package com.softyorch.taskapp.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.Settings
import com.softyorch.taskapp.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    private val _settingsList = MutableStateFlow<List<Settings>>(emptyList())
    val settingsList = _settingsList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSettings().distinctUntilChanged()
                .collect { listOfSettings ->
                    if (listOfSettings.isNullOrEmpty()) {
                        //TODO meter la barra de carga para mostrar al usuario
                    } else {
                        _settingsList.value = listOfSettings
                    }
                }
        }
    }

    fun insertPreferences(settings: Settings) = viewModelScope.launch {
        repository.insertSettings(settings = settings)
    }

    fun updatePreferences(settings: Settings) = viewModelScope.launch {
        repository.updateSettings(settings = settings)
    }
}