package com.softyorch.taskapp.ui.components.topAppBarCustom

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.navigation.AppScreens
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.widgets.LogoUserCapitalLetter
import com.softyorch.taskapp.utils.ELEVATION_DP
import com.softyorch.taskapp.utils.emptyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCustom(
    title: String,
    isMainScreen: Boolean = false,
    nameScreen: String,
    navController: NavController
) {

    val viewModel = hiltViewModel<TopAppBarCustomViewModel>()
    val userPicture: String by viewModel.imageUser.observeAsState(initial = emptyString)
    val userName: String by viewModel.userName.observeAsState(initial = emptyString)

    TopAppBar(
        title = {
            TextTABC(title = title)
        },
        modifier = Modifier.shadow(
            elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ),
        navigationIcon = {
            if (!isMainScreen) IconButtonTABC(
                imageVector = Icons.Rounded.ArrowBack,
                text = stringResource(go_to_home),
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route) {
                    navController.backQueue.clear()
                }
            }
        },
        actions = {
            if (nameScreen != AppScreens.MainScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Home, text = stringResource(go_home)
            ) {
                navController.navigate(AppScreensRoutes.MainScreen.route) {
                    navController.backQueue.clear()
                }
            }

            if (nameScreen != AppScreens.HistoryScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.History, text = stringResource(history)
            ) {
                navController.navigate(AppScreensRoutes.HistoryScreen.route)
            }

            if (nameScreen != AppScreens.SettingsScreen.name) IconButtonTABC(
                imageVector = Icons.Rounded.Settings, text = stringResource(settings)
            ) {
                navController.navigate(AppScreensRoutes.SettingsScreen.route)
            }

            if (nameScreen != AppScreens.UserDataScreen.name) IconButtonTABCUser(
                image = userPicture, userName = userName
            ) {
                navController.navigate(AppScreensRoutes.UserDataScreen.route)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.secondary)
    )
}

@Composable
private fun TextTABC(
    title: String
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primaryContainer,
        style = MaterialTheme.typography.bodyMedium,
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
            tint = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@Composable
private fun IconButtonTABCUser(
    image: String?,
    userName: String,
    onClick: () -> Unit
) {
    LogoUserCapitalLetter(
        capitalLetter = (
                if (userName.isNotEmpty()) userName[0] else emptyString).toString().uppercase(),
        size = 30.dp
    ) {
        onClick()
    }
}