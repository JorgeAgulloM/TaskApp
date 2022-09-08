package com.softyorch.taskapp.presentation.screens.userdata

sealed class UserDataInputError {
    data class ErrorInputTextRequest(
        var errorName: Boolean = false,
        var errorEmail: Boolean = false,
        var errorRepeatEmail: Boolean = false,
        var errorPass: Boolean = false,
        var errorRepeatPass: Boolean = false,
        var noError: Boolean = false
    ) : UserDataInputError()

    data class ErrorInputTextResponse(
        val errorName: String = "",
        val errorEmail: String = "",
        val errorRepeatEmail: String = "",
        val errorPass: String = "",
        val errorRepeatPass: String = ""
    ) : UserDataInputError()

    data class ErrorNameNumberOfCharacter(
        var error: Boolean = false,
        val textError: String = "Error en el número de caracteres"
    ) : UserDataInputError()

    object NameError : UserDataInputError()
    object EmailError : UserDataInputError()
    object EmailRepeatError : UserDataInputError()
    object PassError : UserDataInputError()
    object PassRepeatError : UserDataInputError()
}
