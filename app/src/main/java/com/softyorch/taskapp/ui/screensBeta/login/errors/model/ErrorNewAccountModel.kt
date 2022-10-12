package com.softyorch.taskapp.ui.screensBeta.login.errors.model

data class ErrorNewAccountModel(
    var name: Boolean = false,
    var email: Boolean = false,
    var emailRepeat: Boolean = false,
    var emailExists: Boolean = false,
    var pass: Boolean = false,
    var passRepeat: Boolean = false,
    var error: Boolean = false,
) {
    companion object {
        val errorNewAccountModel = ErrorNewAccountModel()
    }
}