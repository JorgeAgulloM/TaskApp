package com.softyorch.taskapp.presentation.screens.userdata

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.presentation.activities.newImageUser
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.utils.ELEVATION_DP
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    getImage: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = "User Data",
                nameScreen = AppScreens.UserDataScreen.name,
                navController = navController,
            )
        }) {

        ContentUserDataScreen(
            it = it,
            navController = navController,
            getImage = getImage
        )
    }
}
//}

@Composable
private fun ContentUserDataScreen(
    it: PaddingValues,
    navController: NavHostController,
    getImage: () -> Unit
) {
    val viewModel = hiltViewModel<UserDataViewModel>()
    val image = viewModel.image.observeAsState().value
    val name: String by viewModel.name.observeAsState(initial = "")
    val email: String by viewModel.email.observeAsState(initial = "")
    val pass: String by viewModel.pass.observeAsState(initial = "")
    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    val newImage = newImageUser.observeAsState().value

    var confirmDialog by remember { mutableStateOf(false) }
    var cancelDialog by remember { mutableStateOf(false) }
    var logOutDialog by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isLoading) CircularIndicatorCustom(text = "Working...")
        if (newImage != null) {
            viewModel.saveUserImage(newImage)
        }
        AsyncImageDataScreen(image) { getImage() }
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldCustomDataScreen(
                text = name, label = "Name", icon = Icons.Rounded.Person
            ) {
                viewModel.viewModelScope.launch {
                    viewModel.onDataChange(
                        name = it,
                        email = email,
                        pass = pass
                    )
                }
            }
            TextFieldCustomDataScreen(
                text = email, label = "Email", icon = Icons.Rounded.Email
            ) {
                viewModel.viewModelScope.launch {
                    viewModel.onDataChange(
                        name = name,
                        email = it,
                        pass = pass
                    )
                }
            }
            TextFieldCustomDataScreen(
                text = pass, label = "Name", icon = Icons.Rounded.Key, password = true
            ) {
                viewModel.viewModelScope.launch {
                    viewModel.onDataChange(
                        name = name,
                        email = email,
                        pass = it
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonCustomDataScreen(text = "Logout", primary = true) {
                cancelDialog = true
            } //viewModel
            Spacer(modifier = Modifier.padding(bottom = 4.dp))
            ButtonCustomDataScreen(text = "Save", enable = saveEnabled, primary = true) {
                confirmDialog = true
            }
            ButtonCustomDataScreen(text = "Cancel", enable = saveEnabled) {
                cancelDialog = true
            }
        }
    }

    if (logOutDialog) UserDataDialog(
        title = "LogOut",
        text = "Are you sure you want to log out?",
        confirmButtonText = "Yes, I sure",
        onDismissRequest = { logOutDialog = false },
        onDismissButtonClick = { logOutDialog = false }
    ) {
        viewModel.logOut()
    }

    if (confirmDialog) UserDataDialog(
        title = "Save user",
        text = "Are you sure about making the changes?",
        confirmButtonText = "Yes, modify it",
        onDismissRequest = { confirmDialog = false },
        onDismissButtonClick = { confirmDialog = false }
    ) {
        viewModel.viewModelScope.launch { viewModel.onUpdateData() }
        confirmDialog = false
    }

    if (cancelDialog) UserDataDialog(
        title = "Cancel change User",
        text = "Are you sure not to make any changes?",
        confirmButtonText = "yes, not make it",
        onDismissRequest = { cancelDialog = false },
        onDismissButtonClick = { cancelDialog = false }
    ) {
        navController.popBackStack()
    }
}

@Composable
private fun AsyncImageDataScreen(image: Uri?, getImage: () -> Unit) {
    var state by remember { mutableStateOf("") }
    Text(text = state, style = TextStyle(color = Color.Red))
    AsyncImage(
        model = image.toString(),
        contentDescription = "Image of user",
        placeholder = painterResource(R.drawable.ic_person_24),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(200.dp)
            .clickable {
                getImage()
            }.shadow(elevation = ELEVATION_DP, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.large
            ),
        onLoading = { state = "..loading" },
        onSuccess = { state = "ok" },
        onError = { state = "error" }
    )
}

@Composable
private fun ButtonCustomDataScreen(
    text: String, enable: Boolean = true, primary: Boolean = false, onclick: () -> Unit
) {
    ButtonCustom(
        onClick = { onclick() },
        text = text,
        enable = enable,
        primary = primary
    )
}

@Composable
private fun TextFieldCustomDataScreen(
    text: String,
    label: String,
    icon: ImageVector,
    password: Boolean = false,
    onTextFieldChanged: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = label,
        placeholder = "Write your ${label.lowercase()}",
        icon = icon,
        contentDescription = "$label of user",
        singleLine = true,
        password = password,
        onTextFieldChanged = { onTextFieldChanged(it) }
    )
}

@Composable
private fun UserDataDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        dismissButton = {
            ButtonCustomDataScreen(text = "Cancel") { onDismissButtonClick() }
        },
        confirmButton = {
            ButtonCustomDataScreen(text = confirmButtonText, primary = true) {
                onConfirmButtonClick()
            }
        },
        title = { Text(text = title) },
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(textAlign = TextAlign.Center)
            )
        },
        icon = {}
    )
}