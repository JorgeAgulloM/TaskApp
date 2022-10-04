package com.softyorch.taskapp.ui.utils

import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ScreenController(
    private val configuration: Configuration
) {
    private val _isLandscape = MutableLiveData<Boolean>()
    val isLandscape: LiveData<Boolean> = _isLandscape

    val maxHeight = configuration.screenHeightDp
    val maxWidth = configuration.screenWidthDp

    fun landScape() {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                _isLandscape.value = true
            }
            else -> {
                _isLandscape.value = false
            }
        }
    }
}
