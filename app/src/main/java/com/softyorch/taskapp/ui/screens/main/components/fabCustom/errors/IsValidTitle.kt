/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.fabCustom.errors

interface IsValidTitle {
    fun isValidTitle(title: String): Boolean = title.length >= 3
}