/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserDataRepository
import com.softyorch.taskapp.data.repository.user.mapToSettingsModel
import java.util.UUID

class LogoutUser(private val repository: UserDataRepository) {
    suspend operator fun invoke(userId: UUID) {
        repository.saveSettings(SettingsModelDomain(
            id = userId,
            rememberMe = false,
            lastLoginDate = null
        ).mapToSettingsModel())
    }
}