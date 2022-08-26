package com.softyorch.taskapp.ui.components.textCustom

import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextCustomViewModel @Inject constructor(
    private val stateLogin: StateLogin
) : ViewModel() {
    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()
}