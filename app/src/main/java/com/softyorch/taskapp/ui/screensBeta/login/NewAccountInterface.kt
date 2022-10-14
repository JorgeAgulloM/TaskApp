package com.softyorch.taskapp.ui.screensBeta.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.database.userdata.mapToUserDataEntity
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsNewAccount
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel

interface NewAccountInterface : WithOutErrorsNewAccount {

    private val _newAccountModel: MutableLiveData<NewAccountModel>
        get() = MutableLiveData<NewAccountModel>(NewAccountModel.newAccountModel)
    val newAccountModel: LiveData<NewAccountModel>
        get() = _newAccountModel

    private val _errorsNewAccount: MutableLiveData<ErrorNewAccountModel>
        get() = MutableLiveData<ErrorNewAccountModel>(ErrorNewAccountModel.errorNewAccountModel)
    val errorsNewAccount: LiveData<ErrorNewAccountModel>
        get() = _errorsNewAccount

    private val _foundErrorNewAccount: MutableLiveData<Boolean>
        get() = MutableLiveData(false)

    private val _isLoadingNewAccount: MutableLiveData<Boolean>
        get() = MutableLiveData(false)
    val isLoadingNewAccount: LiveData<Boolean>
        get() = _isLoadingNewAccount


    fun onNewAccountInputChange(newAccountModel: NewAccountModel) {
        _newAccountModel.value = newAccountModel
        if (_foundErrorNewAccount.value == true) {
            setErrorsNewAccount(withOutErrorsNewAccount(newAccountModel))
        }
    }

    fun setErrorsNewAccount(errors: ErrorNewAccountModel) {
        if (_foundErrorNewAccount.value != true) _foundErrorNewAccount.value = true
        _errorsNewAccount.value = errors
    }

    suspend fun onNewAccountDataSend(
        newAccountModel: NewAccountModel,
        userDataUseCases: UserDataUseCases
    ): Boolean {
        _isLoadingNewAccount.value = true
        withOutErrorsNewAccount(newAccountModel).let { errors ->
            if (!errors.error) {
                addNewUser(
                    newAccountModel.mapToUserDataEntity(),
                    userDataUseCases
                ).also { isError ->
                    errors.apply {
                        name = !isError
                        email = !isError
                        emailRepeat = !isError
                        emailExists = !isError
                        pass = !isError
                        passRepeat = !isError
                        error = !isError
                    }
                    setErrorsNewAccount(errors)

                    _isLoadingNewAccount.value = false
                    return errors.error
                }
            } else {
                setErrorsNewAccount(errors)

                _isLoadingNewAccount.value = false
                return errors.error
            }
        }
    }

    private suspend fun addNewUser(
        userDataEntity: UserDataEntity,
        userDataUseCases: UserDataUseCases
    ): Boolean {
        return try {
            userDataUseCases.newAccountUser(userDataEntity = userDataEntity)
            true
        } catch (ex: Exception) {
            false
        }
    }

}