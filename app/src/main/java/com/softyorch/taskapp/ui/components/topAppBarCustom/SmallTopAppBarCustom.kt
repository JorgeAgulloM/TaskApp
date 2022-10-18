/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.components.topAppBarCustom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.ELEVATION_DP

@Composable
fun SmallTopAppBarCustom(
    isMainScreen: Boolean = false,
    nameScreen: String,
    navController: NavController,
) {

    TopAppBar(
        modifier = Modifier
            .padding(2.dp)
            .height(35.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large),
        backgroundColor = MaterialTheme.colorScheme.onSecondary,
        title = {},
        navigationIcon = {
            if (!isMainScreen) IconButtonTABC(
                imageVector = Icons.Rounded.ArrowBack,
                text = stringResource(R.string.go_to_home),
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route) {
                    navController.backQueue.clear()
                }
            }
        },
        actions = {
            if (nameScreen != AppScreens.SettingsScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Settings, text = stringResource(R.string.settings)
            ) {
                navController.navigate(AppScreensRoutes.SettingsScreen.route)
            }

            if (nameScreen != AppScreens.UserDataScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Person,
                text = stringResource(R.string.content_image_user)
            ) {
                navController.navigate(AppScreensRoutes.UserDataScreen.route)
            }
        },
        elevation = ELEVATION_DP
    )

}

@Composable
private fun IconButtonTABC(
    imageVector: ImageVector, text: String, onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}