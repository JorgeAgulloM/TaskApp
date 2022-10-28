package com.softyorch.taskapp.domain.userdataUseCase

data class UserDataUseCases(
    val getSettings: GetSettings,
    val getUserEmailExist: GetUserEmailExist,
    val loginUser: LoginUser,
    val newAccountUser: NewAccountUser,
    val saveSettings: SaveSettings,
    val updateUser: UpdateUser
)