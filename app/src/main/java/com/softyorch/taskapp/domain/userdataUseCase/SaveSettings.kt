/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.domain.userdataUseCase

import com.softyorch.taskapp.data.repository.user.UserDataRepository
import com.softyorch.taskapp.data.repository.user.mapToSettingsModel

class SaveSettings(private val repository: UserDataRepository) {
    suspend operator fun invoke(settingsModelDomain: SettingsModelDomain){
        repository.saveSettings(settingsModelDomain.mapToSettingsModel())
    }
}