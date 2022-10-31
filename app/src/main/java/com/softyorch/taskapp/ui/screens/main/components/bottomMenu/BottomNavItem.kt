/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    var indexId: Int,
    val name: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
)