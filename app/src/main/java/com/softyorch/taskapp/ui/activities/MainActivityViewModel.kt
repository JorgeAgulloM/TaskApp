package com.softyorch.taskapp.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val datastore: DatastoreUseCases
) : ViewModel() {
    private val _darkSystem = MutableLiveData(false)
    val darkSystem: LiveData<Boolean> = _darkSystem

    private val _lightOrDark = MutableLiveData(false)
    val lightOrDark: LiveData<Boolean> = _lightOrDark

    private val _colorSystem = MutableLiveData(false)
    val colorSystem: LiveData<Boolean> = _colorSystem

    private val _language = MutableLiveData(true)
    val language: LiveData<Boolean> = _language

    init {
        reloadSettings()
    }

    fun reloadSettings() {
        getUserDataSettings()
    }

    private fun getUserDataSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData.invoke().collect { user ->
                _darkSystem.postValue(user.lightDarkAutomaticTheme)
                _lightOrDark.postValue(user.lightOrDarkTheme)
                _colorSystem.postValue(user.automaticColors)
                _language.postValue(user.automaticLanguage)
            }
        }
    }

}