package com.softyorch.taskapp.presentation

sealed class ErrorUserInput{
    data class Error(
        var name: Boolean = false,
        var email: Boolean = false,
        var emailRepeat: Boolean = false,
        var pass: Boolean = false,
        var passRepeat: Boolean = false,
        var error: Boolean = false,
    ): ErrorUserInput()
}
