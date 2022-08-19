package com.softyorch.taskapp.screens.splash

import android.content.SharedPreferences
import android.view.animation.OvershootInterpolator
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.data.Resource
import com.softyorch.taskapp.model.UserData
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.login.AutoLogin
import com.softyorch.taskapp.utils.login.LoginState
import kotlinx.coroutines.delay
import java.time.Instant
import java.util.*

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
    val context = LocalContext.current

    if (!goToAutoLogin) AutoLogin(sharedPreferences = sharedPreferences).let { autoLogin ->
        if (autoLogin.isTheUserActive() == true) {
            val userActive = autoLogin.userActive()
            produceState<Resource<UserData>>(initialValue = Resource.Loading()) {
                value = splashViewModel.logInWithRememberMe(
                    name = userActive.username,
                    pass = userActive.userPass
                )
                if (value.data?.username.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        "Error de Login. Por favor, inicia sesión de nuevo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.value
                .let { data ->
                    data.data?.let { user ->
                        if (user.rememberMe == true) {
                            val timeWeekInMillis = 604800000
                            user.lastLoginDate?.time?.let { timeDiff ->
                                Date.from(Instant.now()).time.minus(timeDiff)
                                timeDiff.compareTo(timeWeekInMillis).let {
                                    goToAutoLogin = true
                                }
                            }
                        }
                    }
                }
        }
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 800, easing = {
                OvershootInterpolator(8f).getInterpolation(it)
            })
        )

        delay(1500L) //2000

        val route = if (goToAutoLogin)
            AppScreensRoutes.MainScreen.route
        else AppScreensRoutes.LoginScreen.route

        navController.navigate(route) {
            popUpTo(AppScreensRoutes.SplashScreen.route) {
                inclusive = true
            }
        }
        navController.popBackStack()
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