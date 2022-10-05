package com.softyorch.taskapp.ui.screens.splash

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.CircularIndicatorCustomDialog
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel
) {

    val goToAutoLogin by viewModel.goToAutologin.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val isError: Boolean by viewModel.isError.observeAsState(initial = true)
    val getImage: String by viewModel.getImage.observeAsState(initial = emptyString)
    val getUrl: String by viewModel.getUrl.observeAsState(initial = emptyString)
    val getAuthor: String by viewModel.getAuthor.observeAsState(initial = emptyString)
    val getUrlAuthor: String by viewModel.getUrlAuthor.observeAsState(initial = emptyString)
    val scale = remember { Animatable(0f) }
    val imageReq = ImageRequest.Builder(LocalContext.current)
        .data(data = getImage)
        .crossfade(true)
        .crossfade(500)
        .error(R.drawable.pexels_polina_kovaleva_5717421)
        .build()

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = {
                //OvershootInterpolator(16f).getInterpolation(it)
                LinearEasing.transform(16f)
            })
        )

        delay(5000L)

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
            .fillMaxHeight()
            .scale(scale.value)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularIndicatorCustomDialog(stringResource(loading_loading))
            } else {
                imageBackground(image = imageReq)
                bodyScreen(getAuthor, getUrl, getUrlAuthor)
                if (isError) {
                    viewModel.isShowError()
                    Toast.makeText(LocalContext.current, "Error to load image", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}

@Composable
private fun imageBackground(image: ImageRequest) {
    AsyncImage(
        model = image,
        contentDescription = "Random Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxHeight()
    )
}

@Composable
private fun bodyScreen(getAuthor: String, getUrl: String, getUrlAuthor: String) {
    val pexelsUrl = "https://www.pexels.com/"
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uriHandler = LocalUriHandler.current

        AppTitle()
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 40.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailsImageFromPexels(text = "Courtesy of Pexels.com") { uriHandler.openUri(pexelsUrl) }
            DetailsImageFromPexels(text = "Author: $getAuthor") { uriHandler.openUri(getUrlAuthor) }
            DetailsImageFromPexels(text = "Click to view image") { uriHandler.openUri(getUrl) }
        }
    }
}

@Composable
private fun AppTitle() {
    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                shape = MaterialTheme.shapes.large
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(app_name),
            style = MaterialTheme.typography.displayLarge.copy(
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DetailsImageFromPexels(
    text: String,
    onClick: () -> Unit
) {
    var click by remember { mutableStateOf(value = false) }
    val clickColor by click.contentColorLabelAsStateAnimation {
        if (click) {
            onClick()
            click = false
        }
    }

    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .background(
                color = clickColor.copy(alpha = 0.9f),
                shape = MaterialTheme.shapes.large
            ).clickable {
                click = true
            },
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                textDecoration = TextDecoration.Underline
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
