package com.softyorch.taskapp.ui.screensBeta.login

import androidx.lifecycle.MutableLiveData
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.database.userdata.mapToUserDataEntity
import com.softyorch.taskapp.domain.userdataUseCase.UpdateUser
import com.softyorch.taskapp.ui.screensBeta.login.errors.WithOutErrorsNewAccount
import com.softyorch.taskapp.ui.screensBeta.login.errors.model.ErrorNewAccountModel
import com.softyorch.taskapp.ui.screensBeta.login.model.NewAccountModel
import kotlin.reflect.KProperty0

interface NewAccountInterface : WithOutErrorsNewAccount {

    val newAccountModelInterface: MutableLiveData<NewAccountModel>
        get() = MutableLiveData<NewAccountModel>(NewAccountModel.newAccountModel)

    val errorsNewAccountInterface: MutableLiveData<ErrorNewAccountModel>
        get() = MutableLiveData<ErrorNewAccountModel>(ErrorNewAccountModel.errorNewAccountModel)

    val isLoadingNewAccountInterface: MutableLiveData<Boolean>
        get() = MutableLiveData(false)

    val foundErrorNewAccountInterface: MutableLiveData<Boolean>
        get() = MutableLiveData(false)

    fun onNewAccountInputChange(newAccountModel: NewAccountModel) {
        newAccountModelInterface.value = newAccountModel
        if (foundErrorNewAccountInterface.value == true) {
            setErrorsNewAccount(withOutErrorsNewAccount(newAccountModel))
        }
    }

    fun setErrorsNewAccount(errors: ErrorNewAccountModel) {
        if (foundErrorNewAccountInterface.value != true) foundErrorNewAccountInterface.value = true
        errorsNewAccountInterface.value = errors
    }

    suspend fun onNewAccountDataSend(
        newAccountModel: NewAccountModel,
        updateUser: KProperty0<UpdateUser>
    ): Boolean {
        isLoadingNewAccountInterface.value = true
        withOutErrorsNewAccount(newAccountModel).let { errors ->
            if (!errors.error) {
                addNewUser(
                    newAccountModel.mapToUserDataEntity(),
                    updateUser
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

                    isLoadingNewAccountInterface.value = false
                    return errors.error
                }
            } else {
                setErrorsNewAccount(errors)

                isLoadingNewAccountInterface.value = false
                return errors.error
            }
        }
    }

    private suspend fun addNewUser(
        userDataEntity: UserDataEntity,
        updateUser: KProperty0<UpdateUser>
    ): Boolean {
        return try {
            updateUser().invoke(userDataEntity) /**OJO A ESTO*/
            true
        } catch (ex: Exception) {
            false
        }
    }

}