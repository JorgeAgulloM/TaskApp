package com.softyorch.taskapp.screens.splash

import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: UserDataRepository): ViewModel() {
    suspend fun logInWithRememberMe(name: String, pass: String): Resource<UserData> =
        repository.signInSharePreferences(name = name, password = pass)
}