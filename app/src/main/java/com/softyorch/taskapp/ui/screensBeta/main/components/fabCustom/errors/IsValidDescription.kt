/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.fabCustom.errors

interface IsValidDescription {
    fun isValidDescription(description: String) = description.length >= 3
}