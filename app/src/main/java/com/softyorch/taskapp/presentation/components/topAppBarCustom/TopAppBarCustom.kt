package com.softyorch.taskapp.presentation.components.topAppBarCustom

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.theme.LightMode90t

@Composable
fun TopAppBarCustom(
    title: String,
    isMainScreen: Boolean = false,
    nameScreen: String,
    navController: NavController
) {

    val viewModel = hiltViewModel<TopAppBarCustomViewModel>()
    val userPicture = viewModel.getUserPicture()
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
                text = "Go Back",
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route)
            }
        },
        actions = {
            if (nameScreen != AppScreens.MainScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Home, text = "Go Home"
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route)
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

            if (nameScreen != AppScreens.UserDataScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.SupervisedUserCircle, text = "User data",
            ) {
                navController.navigate(AppScreensRoutes.UserDataScreen.route + "/${"0"}")
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