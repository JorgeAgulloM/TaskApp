package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.UserDataRepository
import java.io.IOException
import java.io.InvalidClassException
import javax.inject.Inject

class NewAccountUserUseCase @Inject constructor(private val repository: UserDataRepository) {

    suspend operator fun invoke(userDataEntity: UserDataEntity) = try {
        repository.addUserData(userDataEntity = userDataEntity)
    } catch (ex: IOException) {
        throw InvalidClassException(
            "El usuario no ha podido crearse",
            "Message: ${ex.message.toString()}"
        )
    }

}