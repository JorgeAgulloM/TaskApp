/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.settings

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.models.SettingsModelUi
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.extensions.toStringFormatted

@Composable
fun AnimatedBlock(
    visible: Boolean,
    settings: SettingsModelUi,
    isLoading: Boolean,
    funcOfViewModel: (Int) -> Unit
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column() {
            Row(modifier = Modifier.padding(start = 32.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                content = {
                    Icon(
                        Icons.Rounded.Info,
                        contentDescription = stringResource(R.string.content_last_time_login),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    RowInfo(
                        text = stringResource(R.string.last_login_manual) + " " +
                                settings.lastLoginDate?.toStringFormatted(),
                        paddingStart = 8.dp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            )

            settings.timeLimitAutoLoading =
                sliderCustom(
                    initValue = settings.timeLimitAutoLoading,
                    enable = isLoading,
                    text = stringResource(R.string.time_automatic_login) + " " +
                            getString(settings.timeLimitAutoLoading)
                ) {
                    funcOfViewModel(it)
                }
        }
    }
}