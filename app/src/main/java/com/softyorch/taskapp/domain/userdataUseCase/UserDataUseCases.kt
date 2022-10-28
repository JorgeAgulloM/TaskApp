package com.softyorch.taskapp.domain.userdataUseCase

data class UserDataUseCases(
    val getSettings: GetSettings,
    val getUser: GetUser,
    val getUserEmailExist: GetUserEmailExist,
    val loginUser: LoginUser,
    val logoutUser: LogoutUser,
    val newAccountUser: NewAccountUser,
    val saveSettings: SaveSettings,
    val saveUser: SaveUser,
    val updateUser: UpdateUser
)