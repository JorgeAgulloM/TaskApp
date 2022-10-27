/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.fabCustom.errors

import com.softyorch.taskapp.ui.screens.main.components.fabCustom.model.NewTaskModel

interface WithOutErrorsNewTask : IsValidTitle, IsValidDescription, IsActivatedButton {
    fun withOutErrors(newTaskModel: NewTaskModel): ErrorsNewTaskModel {
        val errors = ErrorsNewTaskModel()
        newTaskModel.apply {
            !isValidTitle(title).also { errors.title = !it }
            !isValidDescription(description).also { errors.description = !it }
            isActivated(newTaskModel).also { errors.isActivatedButton = it }
        }
        errors.apply { error = (title || description) }

        return errors
    }
}