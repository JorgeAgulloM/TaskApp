package com.softyorch.taskapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimaryDark,
    primaryContainer = AppPrimaryContainerDark,
    onPrimary = AppOnPrimaryDark,
    onPrimaryContainer = AppOnPrimaryContainerDark,
    //inversePrimary = Inverse,
    secondary = AppSecondaryDark,
    secondaryContainer = AppSecondaryContainerDark,
    onSecondary = AppOnSecondaryDark,
    onSecondaryContainer = AppOnSecondaryContainerDark,
    tertiary = AppTertiaryDark,
    tertiaryContainer = AppTertiaryContainerDark,
    onTertiary = AppOnTertiaryDark,
    onTertiaryContainer = AppOnTertiaryContainerDark,
    surface = AppSurfaceDark,
    //surfaceTint = ,
    surfaceVariant = AppSurfaceVariantDark,
    onSurface = AppOnSurfaceDark,
    //inverseOnSurface = ,
    //inverseSurface = ,
    onSurfaceVariant = AppOnSurfaceVariantDark,
    background = AppBackgroundDark,
    onBackground = AppOnBackgroundDark,
    error = AppErrorDark,
    errorContainer = AppErrorContainerDark,
    onError = AppOnErrorDark,
    onErrorContainer = AppOnErrorContainerDark,
    outline = AppOutLineDark,
    //outlineVariant = ,
    //scrim =
)

private val LightColorScheme = lightColorScheme(
    primary = AppPrimaryLight,
    primaryContainer = AppPrimaryContainerLight,
    onPrimary = AppOnPrimaryLight,
    onPrimaryContainer = AppOnPrimaryContainerLight,
    //inversePrimary = Inverse,
    secondary = AppSecondaryLight,
    secondaryContainer = AppSecondaryContainerLight,
    onSecondary = AppOnSecondaryLight,
    onSecondaryContainer = AppOnSecondaryContainerLight,
    tertiary = AppTertiaryLight,
    tertiaryContainer = AppTertiaryContainerLight,
    onTertiary = AppOnTertiaryLight,
    onTertiaryContainer = AppOnTertiaryContainerLight,
    surface = AppSurfaceLight,
    //surfaceTint = ,
    surfaceVariant = AppSurfaceVariantLight,
    onSurface = AppOnSurfaceLight,
    //inverseOnSurface = ,
    //inverseSurface = ,
    onSurfaceVariant = AppOnSurfaceVariantLight,
    background = AppBackgroundLight,
    onBackground = AppOnBackgroundLight,
    error = AppErrorLight,
    errorContainer = AppErrorContainerLight,
    onError = AppOnErrorLight,
    onErrorContainer = AppOnErrorContainerLight,
    outline = AppOutLineLight,
    //outlineVariant = ,
    //scrim =

)

@Composable
fun TaskAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}