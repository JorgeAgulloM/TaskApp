package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository

class UpdateUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(userDataEntity: UserDataEntity) =
        repository.updateUserData(userDataEntity = userDataEntity)
}