/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.commonErrors.model

data class ErrorAccountModel(
    var name: Boolean = false,
    var email: Boolean = false,
    var emailRepeat: Boolean = false,
    var emailExists: Boolean = false,
    var pass: Boolean = false,
    var passRepeat: Boolean = false,
    var error: Boolean = false,
    var isActivatedButton: Boolean = false
) {
    companion object {
        val errorAccountModel = ErrorAccountModel()
    }
}