/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors

data class ErrorsNewTaskModel(
    var title: Boolean = false,
    var description: Boolean = false,
    var error: Boolean = false,
    var isActivatedButton: Boolean = false
)
