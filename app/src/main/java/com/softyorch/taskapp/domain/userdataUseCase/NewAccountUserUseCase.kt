package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository
import javax.inject.Inject

class NewAccountUserUseCase @Inject constructor(private val repository: UserDataRepository) {
    suspend operator fun invoke(userDataEntity: UserDataEntity): Boolean =
        repository.addUserData(userDataEntity = userDataEntity)
}