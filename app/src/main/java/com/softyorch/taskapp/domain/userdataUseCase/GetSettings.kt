/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserDataRepository
import kotlinx.coroutines.flow.map

class GetSettings(private val repository: UserDataRepository) {
    operator fun invoke() = repository.getSettings().map {
        it.mapToSettingsModelDomain()
    }
}