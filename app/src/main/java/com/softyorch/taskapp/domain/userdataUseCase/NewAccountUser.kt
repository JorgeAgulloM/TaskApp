package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.data.repository.user.UserDataRepository
import java.io.IOException
import java.io.InvalidClassException

class NewAccountUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(userDataEntity: UserDataEntity): Boolean = try {
        repository.getUserDataEmail(userDataEntity.userEmail).let {
            if (it == null){
                repository.addUserData(userDataEntity = userDataEntity)
                true
            } else false
        }
    } catch (ex: IOException) {
        throw InvalidClassException(
            "Error", "Message: ${ex.message.toString()}"
        )
    }
}