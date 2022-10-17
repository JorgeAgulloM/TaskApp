package com.softyorch.taskapp.ui.screensBeta.login.errors.model

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
