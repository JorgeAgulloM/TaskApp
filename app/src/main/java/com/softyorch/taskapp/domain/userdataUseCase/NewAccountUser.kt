package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository
import java.io.IOException
import java.io.InvalidClassException

class NewAccountUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(userDataEntity: UserDataEntity) = try {
        repository.addUserData(userDataEntity = userDataEntity)
    } catch (ex: IOException) {
        throw InvalidClassException(
            "Error",
            "Message: ${ex.message.toString()}"
        )
    }
}