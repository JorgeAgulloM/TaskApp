package com.softyorch.taskapp.presentation.screens.userdata

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.activities.newImageGallery
import com.softyorch.taskapp.presentation.components.ButtonCustom
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.components.textFieldCustom
import com.softyorch.taskapp.presentation.components.CircularIndicatorCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.LogoUserCapitalLetter
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.emptyString
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    getUserImage: Pair<() -> Unit, String?>
) {
    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(user_data),
                nameScreen = AppScreens.UserDataScreen.name,
                navController = navController,
            )
        }) {

        ContentUserDataScreen(
            it = it,
            navController = navController,
            getUserImage = getUserImage
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun ContentUserDataScreen(
    it: PaddingValues,
    navController: NavHostController,
    getUserImage: Pair<() -> Unit, String?>
) {
    val viewModel = hiltViewModel<UserDataViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    if (isLoading)
        CircularIndicatorCustom(text = stringResource(loading_loading))

    val mainImage: String by newImageGallery.observeAsState(initial = emptyString)
    val savingImage: Boolean by viewModel.savingImage.observeAsState(initial = false)
    val image: String by viewModel.image.observeAsState(initial = emptyString)
    mainImage.let { new ->
        image.let { old ->
            when (new != emptyString && old != new) {
                true -> if (!savingImage) viewModel.onImageChange(new)
                false -> if (savingImage) viewModel.resetSavingImage()
            }
        }
    }
    val name: String by viewModel.name.observeAsState(initial = emptyString)
    val email: String by viewModel.email.observeAsState(initial = emptyString)
    val pass: String by viewModel.pass.observeAsState(initial = emptyString)
    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)

    var confirmDialog by remember { mutableStateOf(value = false) }
    var cancelDialog by remember { mutableStateOf(value = false) }
    var logOutDialog by remember { mutableStateOf(value = false) }

    val errors by rememberSaveable { mutableStateOf(UserDataInputError.ErrorInputText())}

    Column(
        modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding() * 1.5f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        AsyncImageDataScreen(image = image, userName = name) { getUserImage.first() }
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {

            TextFieldCustomDataScreen(
                text = name, label = stringResource(R.string.name),
                icon = Icons.Rounded.Person
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
                text = email, label = stringResource(R.string.email),
                icon = Icons.Rounded.Email,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email
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
                text = pass, label = stringResource(password),
                icon = Icons.Rounded.Key,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go, password = true,
                keyboardActions = KeyboardActions(
                    onGo = {
                        confirmDialog = true
                    }
                )
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
            ButtonCustomDataScreen(text = stringResource(logout), primary = true) {
                logOutDialog = true
            } //viewModel
            Spacer(modifier = Modifier.padding(bottom = 4.dp))
            ButtonCustomDataScreen(
                text = stringResource(save),
                enable = saveEnabled,
                primary = true
            ) {
                confirmDialog = true
            }
            ButtonCustomDataScreen(text = stringResource(cancel), enable = saveEnabled) {
                cancelDialog = true
            }
        }
    }

    if (logOutDialog) UserDataDialog(
        title = stringResource(logout),
        text = stringResource(sure_you_want_logout),
        confirmButtonText = stringResource(yes_sure),
        onDismissRequest = { logOutDialog = false },
        onDismissButtonClick = { logOutDialog = false }
    ) {
        viewModel.logOut()
        logOutDialog = false
        navController.navigate(AppScreensRoutes.SplashScreen.route) {
            navController.backQueue.clear()
        }

    }

    if (confirmDialog) UserDataDialog(
        title = stringResource(save_user),
        text = stringResource(sure_about_making_changes),
        confirmButtonText = stringResource(yes_modify_it),
        onDismissRequest = { confirmDialog = false },
        onDismissButtonClick = { confirmDialog = false }
    ) {
        viewModel.onUpdateData(name = name, email = email, pass = pass, image = image, errors = errors)
        confirmDialog = false
    }

    if (cancelDialog) UserDataDialog(
        title = stringResource(cancel_change_user),
        text = stringResource(sure_not_make_changes),
        confirmButtonText = stringResource(yes_not_make_it),
        onDismissRequest = { cancelDialog = false },
        onDismissButtonClick = { cancelDialog = false }
    ) {
        navController.popBackStack()
    }

}

@Composable
private fun AsyncImageDataScreen(
    image: String,
    userName: String,
    getImage: () -> Unit
) {

/*    var onError by rememberSaveable { mutableStateOf(value = false) }
    if (onError) AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .error(R.drawable.ic_error_outline_24)
            .crossfade(true)
            .crossfade(500)
            .build(),
        contentDescription = stringResource(content_image_user),
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
        onLoading = {
            onError = false
        },
        onSuccess = {
        },
        onError = {
            onError = true
            *//*coroutineScope.launch {
                delay(2000)
                reload(image)
            }*//*
        },
    )
    else*/
    val context = LocalContext.current
    val text = stringResource(func_not_active)

    LogoUserCapitalLetter(
        capitalLetter = (
                if (userName.isNotEmpty()) userName[0] else emptyString).toString().uppercase(),
        size = 200.dp
    ) {
        //getImage()
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
private fun ButtonCustomDataScreen(
    text: String, enable: Boolean = true, primary: Boolean = false, onclick: () -> Unit
) {
    ButtonCustom(
        text = text,
        enable = enable,
        primary = primary,
        error = true
    ){
        onclick()
    }
}

@Composable
private fun TextFieldCustomDataScreen(
    text: String,
    label: String,
    icon: ImageVector,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    password: Boolean = false,
    onTextFieldChanged: (String) -> Unit
) {
    textFieldCustom(
        text = text,
        label = label,
        placeholder = stringResource(write_your_label) + label.lowercase(),
        icon = icon,
        contentDescription = label + stringResource(label_of_user),
        keyboardOptions = KEYBOARD_OPTIONS_CUSTOM.copy(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
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
            ButtonCustomDataScreen(text = stringResource(cancel)) { onDismissButtonClick() }
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