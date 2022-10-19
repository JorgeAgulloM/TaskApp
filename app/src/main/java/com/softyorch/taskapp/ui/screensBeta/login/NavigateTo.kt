/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login

import androidx.navigation.NavController
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes

fun navigateTo(navController: NavController) {
    navController.navigate(AppScreensRoutes.MainScreenBeta.route) {
        navController.backQueue.clear()
    }
}