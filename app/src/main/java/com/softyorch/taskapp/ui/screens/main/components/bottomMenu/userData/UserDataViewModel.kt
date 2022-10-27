/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.models.AccountModel
import com.softyorch.taskapp.ui.models.mapToAccountModel
import com.softyorch.taskapp.ui.screens.commonErrors.WithOutErrorsAccount
import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), WithOutErrorsAccount {
    private val _userDataEntityActive = MutableLiveData<AccountModel>()
    val userDataEntityActive: LiveData<AccountModel> = _userDataEntityActive

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

    fun onDataInputChange(userData: AccountModel) {
        _userDataEntityActive.value = userData
        _saveEnabled.value = true
        if (_foundError.value == true) {
            setErrorsData(errors = withOutErrorsAccount(userData))
        }
    }

    fun onUpdateDataSend(
        userData: AccountModel
    ): Boolean {
        _isLoading.value = true
        withOutErrorsAccount(userData).let { error ->
            setErrorsData(errors = error)
            if (!error.error) {
                _userDataEntityActive.value = userData
                viewModelScope.launch(Dispatchers.IO) {
                    userDataEntityActive.value?.let { userData ->
                        updateUserData(accountModel = userData)
                        updateUserDataDatastore(accountModel = userData)
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
        getData().catch {
            _showErrorLoadData.postValue(true)
            _isLoading.postValue(false)
        } .collect { data ->
            _userDataEntityActive.postValue(data.mapToAccountModel())
            _isLoading.postValue(false)
        }
    }

    fun logOut() = viewModelScope.launch(Dispatchers.IO) { datastore.deleteData() }

    private suspend fun updateUserData(accountModel: AccountModel) {
        //userDataUseCases.updateUser(userDataEntity = accountModel)
    }


    private fun getData() = datastore.getData()

    private suspend fun updateUserDataDatastore(accountModel: AccountModel) {
        //datastore.saveData(userDataEntity = accountModel)
    }
}