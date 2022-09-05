package com.softyorch.taskapp.presentation.components.topAppBarCustom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.softyorch.taskapp.R
import com.softyorch.taskapp.presentation.activities.newImageGallery
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.theme.LightMode90t
import com.softyorch.taskapp.utils.ELEVATION_DP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    title: String,
    isMainScreen: Boolean = false,
    nameScreen: String,
    navController: NavController
) {

    val viewModel = hiltViewModel<TopAppBarCustomViewModel>()
    val userPicture: String by viewModel.imageUser.observeAsState(initial = "")
    val textSizes = viewModel.sizeSelectedOfUser()

    SmallTopAppBar(
        modifier = Modifier.shadow(
            elevation = 4.dp, shape = MaterialTheme.shapes.large.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ),
        title = {
            TextTABC(title = title, textSize = textSizes.largeSize)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.secondary),
        navigationIcon = {
            if (!isMainScreen) IconButtonTABC(
                imageVector = Icons.Rounded.ArrowBack,
                text = "Go To Home",
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route){
                    navController.backQueue.clear()
                }
            }
        },
        actions = {
            if (nameScreen != AppScreens.MainScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Home, text = "Go Home"
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route){
                    navController.backQueue.clear()
                }
            }

            if (nameScreen != AppScreens.HistoryScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.History, text = "History"
            ) {
                navController.navigate(AppScreensRoutes.HistoryScreen.route)
            }

            if (nameScreen != AppScreens.SettingsScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Settings, text = "Settings"
            ) {
                navController.navigate(AppScreensRoutes.SettingsScreen.route)
            }

            if (nameScreen != AppScreens.UserDataScreen.name) IconButtonTABCUser(
                image = userPicture, text = "User data",
            ) {
                navController.navigate(AppScreensRoutes.UserDataScreen.route)
            }
        }
    )
}

@Composable
private fun TextTABC(
    title: String,
    textSize: TextUnit
) {
    Text(
        text = title,
        color = LightMode90t,
        style = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = textSize
        ),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun IconButtonTABC(
    imageVector: ImageVector, text: String, onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun IconButtonTABCUser(
    image: String?,
    text: String,
    onClick: () -> Unit
) {
    IconButton(onClick = { onClick() }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .error(R.drawable.ic_error_outline_24)
                .build(),
            contentDescription = "Image of user",
            placeholder = painterResource(R.drawable.ic_person_24),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large
                ),
            onLoading = {
            },
            onSuccess = {
            },
            onError = {
                /*coroutineScope.launch {
                    delay(2000)
                    reload(image)
                }*/
            },
        )
    }
}