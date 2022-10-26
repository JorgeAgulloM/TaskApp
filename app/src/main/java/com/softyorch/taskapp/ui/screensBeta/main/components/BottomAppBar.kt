/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.R
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.screens.settings.SettingsViewModel
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.SMALL_TOP_BAR_HEIGHT
import com.softyorch.taskapp.utils.extensions.toStringFormatted
import com.softyorch.taskapp.utils.sdk29AndUp
import com.softyorch.taskapp.utils.sdk31AndUp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomFakeNavigationBar(
    index: Int,
    items: List<BottomNavItem>,
    isEnabled: Boolean = true,
    settings: Boolean = false,
    show: Boolean = true,
    onItemClick: (BottomNavItem) -> Unit,
    scope: () -> Unit
) {
    Box(contentAlignment = BottomCenter) {
        BottomMenuSettings(show, settings) { scope() }
        BottomMenuBody(show, isEnabled, items, index, onItemClick)
    }
}

@Composable
private fun BottomMenuBody(
    show: Boolean,
    isEnabled: Boolean,
    items: List<BottomNavItem>,
    index: Int,
    onItemClick: (BottomNavItem) -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = expandVertically(
            animationSpec = tween(durationMillis = 300),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = 300),
            shrinkTowards = Alignment.Bottom
        )
    ) {
        BottomAppBar(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ).shadow(
                elevation = ELEVATION_DP * 2, shape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ),
            backgroundColor = MaterialTheme.colorScheme.background,
            cutoutShape = MaterialTheme.shapes.large,
            elevation = ELEVATION_DP * 2
        ) {
            items.forEach { item ->
                val selected = item.indexId == index
                val gradiant = Brush.verticalGradient(
                    if (selected) listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                    else
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                )
                BottomNavigationItem(
                    selected = selected,
                    onClick = { if (isEnabled) onItemClick(item) },
                    icon = {
                        Column(
                            modifier = Modifier.padding(vertical = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (item.badgeCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Text(
                                            text = item.badgeCount.toString(),
                                            color = if (isEnabled) MaterialTheme.colorScheme.error
                                            else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name
                                    )
                                }
                            } else {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                            if (item.indexId == index) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(2.dp)
                        .background(
                            brush = gradiant,
                            shape = MaterialTheme.shapes.large
                        ),
                    selectedContentColor = if (isEnabled) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.outline,
                    unselectedContentColor = if (isEnabled) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun BottomMenuSettings(show: Boolean, settings: Boolean, scope: () -> Unit) {
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
            Content()
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Content() {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val manualThemeShow: Boolean by viewModel.manualThemeShow.observeAsState(initial = false)
    val settings by viewModel.settings.observeAsState()

    if (settings != null) {
        var visible by rememberSaveable { mutableStateOf(settings!!.rememberMe) }
        SpacerCustom(bottom = 24.dp)
        SwitchCustom(
            text = stringResource(R.string.light_dark_automatic_theme),
            checked = settings!!.lightDarkAutomaticTheme,
            enable = !isLoading && sdk29AndUp,
            description = if (sdk29AndUp) stringResource(R.string.settings_app_adapts_theme)
            else stringResource(R.string.settings_only_android_10)
        ) {
            viewModel.let { vm ->
                vm.applyChanges(
                    settings!!.copy(lightDarkAutomaticTheme = it)
                )
                vm.manualShow(!it)
            }
        }

        if (manualThemeShow) SwitchCustom(
            text = stringResource(R.string.manual_light_dark),
            checked = settings!!.lightOrDarkTheme,
            enable = !isLoading,
            description = stringResource(R.string.settings_switch_light_dark)
        ) {
            viewModel.applyChanges(
                settings!!.copy(lightOrDarkTheme = it)
            )
        }

        /*SwitchCustom(
            text = stringResource(automatic_language),
            checked = settings.automaticLanguage,
            enable = enabled,
            description = stringResource(settings_default_language_or_choose)
        ) {
            settings.automaticLanguage = !settings.automaticLanguage
            enabled = !enabled
            viewModel.applyChanges()
        }*/

        SwitchCustom(
            text = stringResource(R.string.automatic_colors),
            checked = settings!!.automaticColors,
            enable = !isLoading && sdk31AndUp,
            description = if (sdk31AndUp) stringResource(R.string.settings_app_color_adapt)
            else stringResource(R.string.settings_only_android_12)
        ) {
            viewModel.applyChanges(
                settings!!.copy(automaticColors = it)
            )
        }

        SwitchCustom(
            text = stringResource(R.string.remember_me),
            checked = settings!!.rememberMe,
            enable = !isLoading,
            description = stringResource(R.string.settings_autologin_limit_time)
        ) {
            visible = !visible
            viewModel.viewModelScope.launch {
                delay(1000)
                viewModel.applyChanges(
                    settings!!.copy(rememberMe = it)
                )
            }
        }

        AnimatedBlock(visible = visible, settings = settings!!, isLoading = !isLoading) {
            viewModel.applyChanges(
                settings!!.copy(timeLimitAutoLoading = it)
            )
        }

    }

    if (isLoading)
        CircularIndicatorCustom(
            text = stringResource(R.string.loading_loading)
        )
}

@Composable
private fun AnimatedBlock(
    visible: Boolean,
    settings: UserDataEntity,
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
                    androidx.compose.material3.Icon(
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

@Composable
private fun getString(needString: Int): String =
    when (needString) {
        0 -> stringResource(R.string.limit_time_one_day)
        1 -> stringResource(R.string.limit_time_one_week)
        2 -> stringResource(R.string.limit_time_two_week)
        3 -> stringResource(R.string.limit_time_one_month)
        4 -> stringResource(R.string.limit_time_six_month)
        5 -> stringResource(R.string.limit_time_one_year)
        else -> stringResource(R.string.unknown)
    }


