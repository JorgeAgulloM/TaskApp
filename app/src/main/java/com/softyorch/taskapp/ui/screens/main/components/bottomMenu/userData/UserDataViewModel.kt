/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.errors.ErrorInterface
import com.softyorch.taskapp.ui.errors.ErrorUserInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), ErrorInterface {
    private val _userDataEntityActive = MutableLiveData<UserDataEntity>()
    val userDataEntityActive: LiveData<UserDataEntity> = _userDataEntityActive

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    private val _saveEnabled = MutableLiveData<Boolean>()
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorEmail = MutableLiveData<Boolean>()
    val errorEmail: LiveData<Boolean> = _errorEmail

    private val _errorEmailExists = MutableLiveData<Boolean>()
    val errorEmailExists: LiveData<Boolean> = _errorEmailExists

    private val _errorPass = MutableLiveData<Boolean>()
    val errorPass: LiveData<Boolean> = _errorPass

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _errorLoadData = MutableLiveData<Boolean>()
    val errorLoadData: LiveData<Boolean> = _errorLoadData

    private val _foundError = MutableLiveData<Boolean>()

/*    private val _mainImage: MutableLiveData<String?> =
        MutableLiveData<String?>(newImageGallery.value)
    val mainImage: LiveData<String?> = _mainImage*/

    init {
        _isLoading.value = true
        loadUserData()
    }

    fun onDataInputChange(userData: UserDataEntity) {
        _userDataEntityActive.value = userData
        _saveEnabled.value = true
        if (_foundError.value == true) {
            setErrorsData(error = withOutErrors(userData))
        }
    }

    fun onUpdateDataSend(
        userData: UserDataEntity
    ) {
        _isLoading.value = true
        withOutErrors(userData).let { error ->
            setErrorsData(error = error)
            if (!error.error) {
                _userDataEntityActive.value = userData
                viewModelScope.launch(Dispatchers.IO) {
                    userDataEntityActive.value?.let { userData ->
                        updateUserData(userDataEntity = userData)
                        updateUserDataDatastore(userDataEntity = userData)
                    }
                }
            }
            _saveEnabled.postValue(false)
        }
    }

    private fun setErrorsData(error: ErrorUserInput.Error) {
        if (_foundError.value != true) _foundError.postValue(true)
        _errorName.postValue(error.name)
        _errorEmail.postValue(error.email)
        _errorEmailExists.postValue(error.emailExists)
        _errorPass.postValue(error.pass)
        _error.postValue(error.error)
    }

    fun resetErrorLoadData() {
        _errorLoadData.value = false
    }

    /**
     * data
     */

    private suspend fun updateUserData(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity = userDataEntity)

    /**
     * datastore
     */

    fun logOut() = viewModelScope.launch(Dispatchers.IO) { datastore.deleteData() }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        getData().collect { data ->
            _userDataEntityActive.postValue(data)
            _isLoading.postValue(false)
        }
    }

    /** data */

    private fun getData() = datastore.getData()

    private suspend fun updateUserDataDatastore(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity = userDataEntity)
}