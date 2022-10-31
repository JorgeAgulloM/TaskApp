/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SMALL_TOP_BAR_HEIGHT

@Composable
fun BottomMenuUser(
    show: Boolean,
    userData: Boolean,
    navController: NavController,
    scope: () -> Unit
) {
    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 5) * 4
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onTertiary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    AnimatedVisibility(
        visible = show && userData,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Bottom
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = (SMALL_TOP_BAR_HEIGHT + 8).dp,
                    start = 8.dp,
                    end = 8.dp
                )
                .height(calculateHeight.dp)
                .shadow(
                    ELEVATION_DP,
                    shape = MaterialTheme.shapes.large,
                    ambientColor = MaterialTheme.colorScheme.tertiary,
                    spotColor = MaterialTheme.colorScheme.tertiary
                )
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            BodyContentUserData(navController)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonCustom(
                    text = stringResource(go_home),
                    onClick = { scope() }
                )
            }
        }
    }
}