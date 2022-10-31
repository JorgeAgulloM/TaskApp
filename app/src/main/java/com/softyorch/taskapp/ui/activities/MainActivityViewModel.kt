package com.softyorch.taskapp.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.models.mapToSettingsModelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val useCases: UserDataUseCases
) : ViewModel() {
    private val _darkSystem = MutableLiveData(false)
    val darkSystem: LiveData<Boolean> = _darkSystem

    private val _lightOrDark = MutableLiveData(false)
    val lightOrDark: LiveData<Boolean> = _lightOrDark

    private val _colorSystem = MutableLiveData(false)
    val colorSystem: LiveData<Boolean> = _colorSystem

    private val _language = MutableLiveData(true)
    //val language: LiveData<Boolean> = _language

    init {
        reloadSettings()
    }

    private fun reloadSettings() {
        getUserDataSettings()
    }

    private fun getUserDataSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect { user ->
                _darkSystem.postValue(user.lightDarkAutomaticTheme)
                _lightOrDark.postValue(user.lightOrDarkTheme)
                _colorSystem.postValue(user.automaticColors)
                _language.postValue(user.automaticLanguage)
            }
        }
    }

    private fun getData() = useCases.getSettings().map {
        it.mapToSettingsModelUi()
    }

}