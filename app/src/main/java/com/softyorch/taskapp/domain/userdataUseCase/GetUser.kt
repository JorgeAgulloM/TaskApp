/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserDataRepository
import kotlinx.coroutines.flow.map

class GetUser(private val repository: UserDataRepository) {
    operator fun invoke() = repository.getUser().map {
        it.mapToUserModelDomain()
    }
}