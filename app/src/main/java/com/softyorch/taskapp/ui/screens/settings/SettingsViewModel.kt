package com.softyorch.taskapp.ui.screens.settings

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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData().collect {
                _settings.postValue(it)
                _isLoading.postValue(false)
            }
        }
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
            _isLoading.value = false
        }
    }

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity = userDataEntity)

    private suspend fun updateData(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity = userDataEntity)

}

