package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.user.UserDataRepository

class LoginUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(email: String, password: String): UserDataEntity? =
        repository.singInUser(email = email, password = password)

    /**
     * Aquí impolementaré el update del login, se actualizará el valor LastLogin y el rememberMe,
     * y devolverá el usuario actualizado para que este se actualice en el datastore
     * */

}

