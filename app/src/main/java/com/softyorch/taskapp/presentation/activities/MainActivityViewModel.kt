package com.softyorch.taskapp.presentation.activities

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private var stateLogin: StateLogin
) : ViewModel() {

    fun loadSharePreferences(sharedPreferences: SharedPreferences) =
        stateLogin.loadSharedPreferences(sharedPreferences = sharedPreferences)

    fun loadSettings(): List<Any> {
        return stateLogin.loadSettings()
    }

}