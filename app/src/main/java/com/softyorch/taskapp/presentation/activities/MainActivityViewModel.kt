package com.softyorch.taskapp.presentation.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val datastore: DatastoreRepository
) : ViewModel() {
    private val _darkSystem = MutableLiveData<Boolean>()
    val darkSystem: LiveData<Boolean> = _darkSystem
    private val _lightOrDark = MutableLiveData<Boolean>()
    val lightOrDark: LiveData<Boolean> = _lightOrDark
    private val _colorSystem = MutableLiveData<Boolean>()
    val colorSystem: LiveData<Boolean> = _colorSystem
    private val _language = MutableLiveData<Boolean>()
    val language: LiveData<Boolean> = _language

    init {
        getUserDataSettings()
    }

    fun reloadSettings() {
        getUserDataSettings()
    }

    private fun getUserDataSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData().collect {
                _darkSystem.postValue(it.lightDarkAutomaticTheme)
                _lightOrDark.postValue(it.lightOrDarkTheme)
                _colorSystem.postValue(it.automaticColors)
                _language.postValue(it.automaticLanguage)
            }
        }
    }

}