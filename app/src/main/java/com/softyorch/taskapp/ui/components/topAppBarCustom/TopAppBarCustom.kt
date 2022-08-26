package com.softyorch.taskapp.ui.components.topAppBarCustom

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.navigation.AppScreens
import com.softyorch.taskapp.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.theme.LightMode90t

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
            Text(
                text = title,
                color = LightMode90t,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = textSizes.largeSize
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.secondary),
        navigationIcon = {
            if (!isMainScreen) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Go Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            if (nameScreen != AppScreens.MainScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.MainScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Go Home",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.HistoryScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.HistoryScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = "History",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.SettingsScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.SettingsScreen.route)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (nameScreen != AppScreens.UserDataScreen.name) {
                IconButton(onClick = {
                    navController.navigate(AppScreensRoutes.UserDataScreen.route + "/${"0"}")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.SupervisedUserCircle,
                        contentDescription = "User data",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}