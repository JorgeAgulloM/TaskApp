package com.softyorch.taskapp.screens.splash

import android.content.SharedPreferences
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.AutoLogin
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashViewModel: SplashViewModel,
    sharedPreferences: SharedPreferences
) {
    val scale = remember {
        Animatable(0f)
    }

    var goToAutoLogin by rememberSaveable { mutableStateOf(value = false) }

    if (!goToAutoLogin) AutoLogin(sharedPreferences = sharedPreferences).let { autoLogin ->

        Log.d("AUTOLOGIN", "Entrando en AutoLogin")

        if (autoLogin.isTheUserActive() == true) {
            val userActive = autoLogin.userActive()

            /** Añadir lógica para comprobar el tiempo transcurrido desde el último login*/

            Log.d("AUTOLOGIN", "AutoLogin = True")

            produceState<Resource<UserData>>(initialValue = Resource.Loading()) {
                Log.d("AUTOLOGIN", "Accediendo a produceState")
                value = splashViewModel.logInWithRememberMe(
                    name = userActive.username,
                    pass = userActive.userPass
                )
                if (value.data?.username.isNullOrEmpty()) {
                    Log.d("AUTOLOGIN", "Sin Autologin por vacío o nulo")
                }
            }.value
                .let { data ->
                    data.data?.let {
                        Log.d("AUTOLOGIN", "si hay usuario")

                        goToAutoLogin = true
                    }
                }

        } else Log.d("AUTOLOGIN", "AutoLogin = False")
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800, easing = {
                OvershootInterpolator(8f).getInterpolation(it)
            })
        )

        delay(2000L)
        navController.popBackStack()
        if (goToAutoLogin)
            navController.navigate(AppScreensRoutes.MainScreen.route)
        else
            navController.navigate(AppScreensRoutes.LoginScreen.route)

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
            Image(
                painter = painterResource(id = R.drawable.notes_512x512),
                contentDescription = "Splash image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(95.dp)
            )
            Text(
                text = "Task App",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}