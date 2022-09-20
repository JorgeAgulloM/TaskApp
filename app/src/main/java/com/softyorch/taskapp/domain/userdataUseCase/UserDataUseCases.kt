package com.softyorch.taskapp.domain.userdataUseCase

data class UserDataUseCases(
    val getUserEmailExist: GetUserEmailExist,
    val loginUser: LoginUser,
    val newAccountUser: NewAccountUser,
    val updateUser: UpdateUser
)