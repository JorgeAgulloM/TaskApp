/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.domain.userdataUseCase.mapToUserModelDomain
import com.softyorch.taskapp.ui.models.UserModelUi
import com.softyorch.taskapp.ui.models.mapToUserModelUI
import com.softyorch.taskapp.ui.screens.commonErrors.WithOutErrorsAccount
import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), WithOutErrorsAccount {
    private val _user = MutableLiveData<UserModelUi>()
    val user: LiveData<UserModelUi> = _user

    private val _errorsAccount = MutableLiveData(ErrorAccountModel.errorAccountModel)
    val errorsAccount: LiveData<ErrorAccountModel> = _errorsAccount

    private val _saveEnabled = MutableLiveData<Boolean>()
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showErrorLoadData = MutableLiveData<Boolean>()
    val showErrorLoadData: LiveData<Boolean> = _showErrorLoadData

    private val _foundError = MutableLiveData<Boolean>()

    init {
        _isLoading.value = true
        loadUserData()
    }

    fun onDataInputChange(userData: UserModelUi) {
        _user.value = userData
        _saveEnabled.value = true
        if (_foundError.value == true) {
            setErrorsData(errors = withOutErrorsAccount(userData))
        }
    }

    fun onUpdateDataSend(
        userData: UserModelUi
    ): Boolean {
        _isLoading.value = true
        withOutErrorsAccount(userData).let { error ->
            setErrorsData(errors = error)
            if (!error.error) {
                _user.value = userData
                viewModelScope.launch(Dispatchers.IO) {
                    user.value?.let { userData ->
                        updateUserData(userModelUi = userData)
                    }
                }
            }
            _isLoading.postValue(false)
            _saveEnabled.postValue(false)
            return error.error
        }
    }

    private fun setErrorsData(errors: ErrorAccountModel) {
        if (_foundError.value != true) _foundError.postValue(true)
        _errorsAccount.postValue(errors)
    }

    fun resetShowErrorLoad() {
        _showErrorLoadData.value = false
    }

    /**
     * data
     */

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        getData()
            .catch {
                _showErrorLoadData.postValue(true)
                _isLoading.postValue(false)
            }
            .collect { data ->
                _user.postValue(data)
                _isLoading.postValue(false)

            }
    }

    private fun getData() = userDataUseCases.getUser().map {
        it.mapToUserModelUI()
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) { userDataUseCases.logoutUser(user.value!!.id) }
    }

    private suspend fun updateUserData(userModelUi: UserModelUi) {
        userDataUseCases.saveUser(userModelUi.mapToUserModelDomain())
    }

}