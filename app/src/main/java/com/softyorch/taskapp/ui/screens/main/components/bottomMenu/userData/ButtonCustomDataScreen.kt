/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.compose.runtime.Composable
import com.softyorch.taskapp.ui.components.ButtonCustom

@Composable
fun ButtonCustomDataScreen(
    text: String,
    enable: Boolean = true,
    primary: Boolean = false,
    tertiary: Boolean = false,
    error: Boolean = false,
    onclick: () -> Unit
) {
    ButtonCustom(
        text = text,
        enable = enable,
        tertiary = tertiary,
        primary = primary,
        error = error
    ) {
        onclick()
    }
}