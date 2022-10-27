/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors.model

data class ErrorLoginModel(
    var email: Boolean = false,
    var pass: Boolean = false,
    var errorResultSignIn: Boolean = false,
    var error: Boolean = false,
    var isActivatedButton: Boolean = false
) {
    companion object {
        val errorLoginModel = ErrorLoginModel()
    }
}
