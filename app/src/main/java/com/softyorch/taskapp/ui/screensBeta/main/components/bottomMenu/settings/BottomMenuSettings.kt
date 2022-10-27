/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.settings

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
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SMALL_TOP_BAR_HEIGHT

@Composable
fun BottomMenuSettings(show: Boolean, settings: Boolean, scope: () -> Unit) {
    val maxHeight = LocalConfiguration.current.screenHeightDp
    val calculateHeight = (maxHeight / 5) * 4
    val sheetBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.secondaryContainer
        )
    )

    AnimatedVisibility(
        visible = show && settings,
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
                .padding(top = (SMALL_TOP_BAR_HEIGHT + 8).dp)
                .height(calculateHeight.dp)
                .shadow(
                    ELEVATION_DP,
                    shape = MaterialTheme.shapes.large
                )
                .background(
                    brush = sheetBrush,
                    shape = MaterialTheme.shapes.large
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            SettingsContent()
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonCustom(
                    text = "Salir",
                    onClick = { scope() }
                )
            }
        }
    }
}