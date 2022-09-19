package com.softyorch.taskapp.ui.activities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.repository.DatastoreRepository
import com.softyorch.taskapp.domain.datastoreUseCase.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase
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
        reloadSettings()
    }

    fun reloadSettings() {
        getUserDataSettings()
    }

    private fun getUserDataSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            getDataUseCase().let { resource ->
                when (resource){
                    is Resource.Error -> {
                        _darkSystem.postValue(true)
                        _lightOrDark.postValue(false)
                        _colorSystem.postValue(true)
                        _language.postValue(true)
                    }
                    is Resource.Loading -> Log.d("Resource", "Resource.getUserDataSettings() -> loading...")
                    is Resource.Success -> {
                        resource.data?.flowOn(Dispatchers.IO)?.collect{ user ->
                            _darkSystem.postValue(user.lightDarkAutomaticTheme)
                            _lightOrDark.postValue(user.lightOrDarkTheme)
                            _colorSystem.postValue(user.automaticColors)
                            _language.postValue(user.automaticLanguage)
                        }
                    }
                }
            }
        }
    }
}