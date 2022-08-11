package com.softyorch.taskapp.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.model.Preferences
import com.softyorch.taskapp.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val repository: PreferencesRepository
) : ViewModel() {
    private val _preferencesList = MutableStateFlow<List<Preferences>>(emptyList())
    val preferencesList = _preferencesList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPreferences().distinctUntilChanged()
                .collect { listOfPreferences ->
                    if (listOfPreferences.isNullOrEmpty()) {
                        //TODO meter la barra de carga para mostrar al usuario
                    } else {
                        _preferencesList.value = listOfPreferences
                    }
                }
        }
    }

    fun insertPreferences(preferences: Preferences) = viewModelScope.launch {
        repository.insertPreferences(preferences = preferences)
    }

    fun updatePreferences(preferences: Preferences) = viewModelScope.launch {
        repository.updatePreferences(preferences = preferences)
    }
}