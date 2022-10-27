/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.navigation.NavController
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.menu.BottomMenuBody
import com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.settings.BottomMenuSettings
import com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.userData.BottomMenuUser

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomFakeNavigationBar(
    index: Int,
    items: List<BottomNavItem>,
    isEnabled: Boolean = true,
    settings: Boolean = false,
    userData: Boolean = false,
    show: Boolean = true,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit,
    scope: () -> Unit
) {
    Box(contentAlignment = BottomCenter) {
        BottomMenuUser(show, userData, navController) { scope() }
        BottomMenuSettings(show, settings) { scope() }
        BottomMenuBody(show, isEnabled, items, index, onItemClick)
    }
}


