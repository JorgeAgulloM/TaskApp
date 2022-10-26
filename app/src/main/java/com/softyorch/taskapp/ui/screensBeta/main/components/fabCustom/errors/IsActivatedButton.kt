/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors

import com.softyorch.taskapp.ui.screensBeta.main.model.NewTaskModel

interface IsActivatedButton {
    fun isActivated(newTaskModel: NewTaskModel) =
        newTaskModel.title.isNotEmpty() && newTaskModel.description.isNotEmpty()
}