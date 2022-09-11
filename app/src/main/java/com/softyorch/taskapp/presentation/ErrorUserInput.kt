package com.softyorch.taskapp.presentation

sealed class ErrorUserInput{
    data class Error(
        val name: Boolean = false,
        val email: Boolean = false,
        val emailRepeat: Boolean = false,
        val pass: Boolean = false,
        val passRepeat: Boolean = false,
        val error: Boolean = false,
    ): ErrorUserInput()
}
