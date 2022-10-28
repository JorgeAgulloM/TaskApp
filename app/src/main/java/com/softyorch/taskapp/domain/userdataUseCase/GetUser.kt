/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserDataRepository

class GetUser(private val repository: UserDataRepository) {
    suspend operator fun invoke() = repository.getUser()?.mapToUserModelDomain()
}