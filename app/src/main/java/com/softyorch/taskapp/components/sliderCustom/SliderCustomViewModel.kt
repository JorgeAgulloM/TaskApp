package com.softyorch.taskapp.components.sliderCustom

import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.StandardizedSizes
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SliderCustomViewModel @Inject constructor(
    private val stateLogin: StateLogin
): ViewModel() {

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()
}