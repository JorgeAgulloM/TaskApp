package com.softyorch.taskapp.ui.screens.settings

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.SwitchCustom
import com.softyorch.taskapp.ui.components.sliderCustom
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.ui.widgets.RowInfo
import com.softyorch.taskapp.utils.extensions.toStringFormatted
import com.softyorch.taskapp.utils.sdk29AndUp
import com.softyorch.taskapp.utils.sdk31AndUp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            SmallTopAppBarCustom(
                isMainScreen = false,
                title = stringResource(settings),
                navController = navController,
                icon = Icons.Rounded.Home,
                scopeSettings = {},
                scopeUserData = {}
            )
        }
    ) {
        Content(it = it)
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun Content(it: PaddingValues) {

    val viewModel = hiltViewModel<SettingsViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    val manualThemeShow: Boolean by viewModel.manualThemeShow.observeAsState(initial = false)
    val settings by viewModel.settings.observeAsState()
    Log.d("MY_SETTINGS", "settings on screen -> $settings")
    val scrollState = rememberScrollState()

    val contentBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondary
        )
    )

    Column(
        modifier = Modifier
            .background(brush = contentBrush)
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(top = it.calculateTopPadding() * 1.5f)
    ) {

        if (settings != null) {
            var visible by rememberSaveable { mutableStateOf(settings!!.rememberMe) }

            SwitchCustom(
                text = stringResource(light_dark_automatic_theme),
                checked = settings!!.lightDarkAutomaticTheme,
                enable = !isLoading && sdk29AndUp,
                description = if (sdk29AndUp) stringResource(settings_app_adapts_theme)
                else stringResource(settings_only_android_10)
            ) {
                viewModel.let { vm ->
                    vm.applyChanges(
                        settings!!.copy(lightDarkAutomaticTheme = it)
                    )
                    vm.manualShow(!it)
                }
            }

            if (manualThemeShow) SwitchCustom(
                text = stringResource(manual_light_dark),
                checked = settings!!.lightOrDarkTheme,
                enable = !isLoading,
                description = stringResource(settings_switch_light_dark)
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
                text = stringResource(automatic_colors),
                checked = settings!!.automaticColors,
                enable = !isLoading && sdk31AndUp,
                description = if (sdk31AndUp) stringResource(settings_app_color_adapt)
                else stringResource(settings_only_android_12)
            ) {
                viewModel.applyChanges(
                    settings!!.copy(automaticColors = it)
                )
            }

            SwitchCustom(
                text = stringResource(remember_me),
                checked = settings!!.rememberMe,
                enable = !isLoading,
                description = stringResource(settings_autologin_limit_time)
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
    }
    if (isLoading)
        CircularIndicatorCustom(
            text = stringResource(loading_loading)
        )
}

@Composable
private fun ColumnScope.AnimatedBlock(
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
                    Icon(
                        Icons.Rounded.Info,
                        contentDescription = stringResource(content_last_time_login),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    RowInfo(
                        text = stringResource(last_login_manual) + " " +
                                settings.lastLoginDate?.toStringFormatted(),
                        paddingStart = 8.dp,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )

            settings.timeLimitAutoLoading =
                sliderCustom(
                    initValue = settings.timeLimitAutoLoading,
                    enable = isLoading,
                    text = stringResource(time_automatic_login) + " " +
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
        0 -> stringResource(limit_time_one_day)
        1 -> stringResource(limit_time_one_week)
        2 -> stringResource(limit_time_two_week)
        3 -> stringResource(limit_time_one_month)
        4 -> stringResource(limit_time_six_month)
        5 -> stringResource(limit_time_one_year)
        else -> stringResource(unknown)
    }

