package com.softyorch.taskapp.ui.screens.userdata

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.ui.components.*
import com.softyorch.taskapp.ui.components.topAppBarCustom.SmallTopAppBarCustom
import com.softyorch.taskapp.utils.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    //getUserImage: Pair<() -> Unit, String?>
) {
    Scaffold(
        topBar = {
            SmallTopAppBarCustom(
                isMainScreen = false,
                title = stringResource(user_data),
                navController = navController,
                icon = Icons.Rounded.Home,
                scopeSettings = {},
                scopeUserData = {}
            )
        }
    ) {
        /*Content(
            it = it,
            navController = navController,
            //getUserImage = getUserImage
        )*/
    }
}

