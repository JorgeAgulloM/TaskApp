/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.components.topAppBarCustom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.sdk29AndUp

@Composable
fun SmallTopAppBarCustom(
    isMainScreen: Boolean = false,
    title: String,
    navController: NavController,
    icon: ImageVector
) {

    TopAppBar(
        modifier = Modifier
            .systemBarsPadding()
            .padding(horizontal = 4.dp, vertical = sdk29AndUp { 0.dp } ?: 8.dp)
            .height(35.dp)
            .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large),
        backgroundColor = MaterialTheme.colorScheme.background,
        title = {
            Text(
                text = if (title == stringResource(R.string.complete))
                    stringResource(R.string.tasks_completed_last_days)
                else title,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        navigationIcon = {
            IconButtonTABC(
                imageVector = icon,
                text = stringResource(R.string.go_to_home),
            ) {
                if (!isMainScreen) navController.navigate(AppScreensRoutes.MainScreenBeta.route) {
                    navController.backQueue.clear()
                }
            }
        },
        actions = {
            IconButtonTABC(
                imageVector = Icons.Rounded.Settings, text = stringResource(R.string.settings)
            ) {
                navController.navigate(AppScreensRoutes.SettingsScreen.route)
            }

            IconButtonTABC(
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
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}