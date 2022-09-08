package com.softyorch.taskapp.presentation.screens.settings


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserDataRepository,
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
        loadUserData()
    }

    fun applyChanges() {
        _isLoading.value = true
        _settings.value?.let {
            updatePreferences(userData = it)
            _isLoading.postValue(false)
        }
    }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getData().collect { userData ->
            _settings.postValue(userData)
            _isLoading.postValue(false)
        }
    }

    private fun updatePreferences(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {
        updateUser(userData = userData)
        updateData(userData = userData)
    }

    private suspend fun updateUser(userData: UserData) =
        repository.updateUserData(userData = userData)

    private suspend fun updateData(userData: UserData) = datastore.saveData(userData = userData)

}

