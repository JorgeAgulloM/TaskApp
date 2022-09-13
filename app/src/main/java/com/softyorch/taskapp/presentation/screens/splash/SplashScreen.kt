package com.softyorch.taskapp.presentation.screens.splash

import android.annotation.SuppressLint
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel
) {

    val goToAutoLogin by viewModel.goToAutologin.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800, easing = {
                OvershootInterpolator(8f).getInterpolation(it)
            })
        )

        delay(1500L)

        if (!isLoading) {
            val route = if (goToAutoLogin)
                AppScreensRoutes.MainScreen.route
            else AppScreensRoutes.LoginScreen.route

            navController.navigate(route) {
                popUpTo(AppScreensRoutes.SplashScreen.route) {
                    inclusive = true
                }
            }
        }
    })

    Surface(
        modifier = Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth(1f)
            .padding(8.dp)
            .size(330.dp)
            .scale(scale.value)
    ) {

        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ImageSplash()
            TextSplash()
        }
    }
}

@Composable
private fun TextSplash() {
    Text(
        text = stringResource(app_name),
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun ImageSplash() {
    Image(
        painter = painterResource(id = R.drawable.notes_512x512),
        contentDescription = stringResource(content_splash_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(95.dp)
    )
}