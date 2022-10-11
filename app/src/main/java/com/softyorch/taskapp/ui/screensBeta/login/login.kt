package com.softyorch.taskapp.ui.screensBeta.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import kotlinx.coroutines.delay

/**
 * Task-App
 * File created by Jorge Agulló on 11/November/2022
 */

@Preview(showBackground = true)
@Composable
fun LoginScreenBeta(navController: NavController = NavController(context = LocalContext.current)) {

    val viewModel = hiltViewModel<LoginViewModelBeta>()

    TaskAppTheme {
        Surface(Modifier.fillMaxSize()) {
            Background()
            Body(viewModel)
        }
    }
}

@Composable
fun Background() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = rememberAsyncImagePainter(
                model = R.drawable.pexels_polina_kovaleva_5717421,
                contentScale = ContentScale.Crop
            ),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun Body(viewModel: LoginViewModelBeta) {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = {
                LinearEasing.transform(16f)
            })
        )

        delay(5000L)

    })
}


