package com.softyorch.taskapp.ui.screens.settings


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.GetDataUseCase
import com.softyorch.taskapp.domain.datastoreUseCase.SaveDataUseCase
import com.softyorch.taskapp.domain.userdataUseCase.GetUserEmailExistUseCase
import com.softyorch.taskapp.domain.userdataUseCase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveDataUseCase: SaveDataUseCase,
    private val getDataUseCase: GetDataUseCase,
    private val getUserEmailExistUseCase: GetUserEmailExistUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _settings = MutableLiveData<UserDataEntity>()
    val settings: LiveData<UserDataEntity> = _settings

    private val _reloading = MutableLiveData<Boolean>()
    val needReload: LiveData<Boolean> = _reloading

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        loadUserData()
    }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        getDataUseCase().let { resource ->
            when (resource) {
                is Resource.Error -> {
                    TODO()
                }
                is Resource.Loading -> Log.d("Resource", "Resource.getUserData() -> loading...")
                is Resource.Success -> {
                    resource.data?.flowOn(Dispatchers.IO)?.collect { data ->
                        getUserEmailExistUseCase(email = data.userEmail).let { user ->
                            _settings.postValue(user)
                            _isLoading.postValue(false)
                        }
                    }
                }
            }
        }
    }

    fun reloaded() {
        _reloading.value = false
        _isLoading.value = false
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
            _reloading.value = true
        }
    }

    private suspend fun updateUser(userDataEntity: UserDataEntity) =
        updateUserUseCase(userDataEntity = userDataEntity)
    private suspend fun updateData(userDataEntity: UserDataEntity) =
        saveDataUseCase(userDataEntity = userDataEntity)

}

