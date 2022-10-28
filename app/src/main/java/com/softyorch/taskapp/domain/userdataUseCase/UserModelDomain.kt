/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserModel
import com.softyorch.taskapp.utils.emptyString
import java.util.*

data class UserModelDomain(
    var id: UUID = UUID.randomUUID(),
    var username: String = emptyString,
    var userEmail: String = emptyString,
    var userPass: String = emptyString,
    var userPicture: String = emptyString,
)

fun UserModel.mapToUserModelDomain() = UserModelDomain(
    id, username, userEmail, userPass, userPicture
)
