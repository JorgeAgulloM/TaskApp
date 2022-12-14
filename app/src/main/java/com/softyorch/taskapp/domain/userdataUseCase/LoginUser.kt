package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository

class LoginUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(email: String, password: String): UserDataEntity? =
        repository.singInUser(email = email, password = password)
}

