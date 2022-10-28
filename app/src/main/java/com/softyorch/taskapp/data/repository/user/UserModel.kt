/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.data.repository.user

import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.userdataUseCase.UserModelDomain
import com.softyorch.taskapp.utils.*
import java.util.*

data class UserModel(
    var id: UUID = UUID.randomUUID(),
    var username: String = emptyString,
    var userEmail: String = emptyString,
    var userPass: String = emptyString,
    var userPicture: String = emptyString,
)

fun UserDataEntity.mapToUserModel() = UserModel(
    id, username, userEmail, userPass, userPicture
)

fun UserModelDomain.mapToUserModel() = UserModel(
    id, username, userEmail, userPass, userPicture
)