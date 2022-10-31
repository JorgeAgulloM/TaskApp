package com.softyorch.taskapp.domain.userdataUseCase


import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.user.UserDataRepository

class GetUserEmailExist(private val repository: UserDataRepository) {
    suspend operator fun invoke(email: String): UserDataEntity? =
        repository.getUserDataEmail(email = email)
}