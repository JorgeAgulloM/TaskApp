package com.softyorch.taskapp.presentation.screens.userdata

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.softyorch.taskapp.presentation.components.*
import com.softyorch.taskapp.presentation.components.topAppBarCustom.TopAppBarCustom
import com.softyorch.taskapp.presentation.navigation.AppScreens
import com.softyorch.taskapp.presentation.navigation.AppScreensRoutes
import com.softyorch.taskapp.presentation.widgets.LogoUserCapitalLetter
import com.softyorch.taskapp.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun UserDataScreen(
    navController: NavHostController,
    getUserImage: Pair<() -> Unit, String?>
) {
    var visibleScreen by remember { mutableStateOf(value = false) }
    rememberCoroutineScope().launch {
        delay(TIME_IN_MILLIS_OF_DELAY)
        visibleScreen = true
    }

    Scaffold(
        topBar = {
            TopAppBarCustom(
                title = stringResource(user_data),
                nameScreen = AppScreens.UserDataScreen.name,
                navController = navController,
            ) {
                visibleScreen = false
            }
        }) {
        AnimatedVisibility(
            visible = visibleScreen,
            enter = ANIMATED_ENTER,
            exit = ANIMATED_EXIT
        ) {
            ContentUserDataScreen(
                it = it,
                navController = navController,
                getUserImage = getUserImage
            )
        }
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

    if (isLoading) CircularIndicatorCustom(text = stringResource(loading_loading))

    val mainImage: String by newImageGallery.observeAsState(initial = emptyString)
    val savingImage: Boolean by viewModel.savingImage.observeAsState(initial = false)
    val image: String by viewModel.image.observeAsState(initial = emptyString)
    mainImage.let { new ->
        image.let { old ->
            when (new != emptyString && old != new) {
                true -> if (!savingImage) viewModel.onImageInputChange(new)
                false -> if (savingImage) viewModel.resetSavingImage()
            }
        }
    }
    val name: String by viewModel.name.observeAsState(initial = emptyString)
    val email: String by viewModel.email.observeAsState(initial = emptyString)
    val pass: String by viewModel.pass.observeAsState(initial = emptyString)
    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)

    /** Error states */
    val errorName: Boolean by viewModel.errorName.observeAsState(initial = false)
    val errorEmail: Boolean by viewModel.errorEmail.observeAsState(initial = false)
    val errorEmailExists: Boolean by viewModel.errorEmailExists.observeAsState(initial = false)
    val errorPass: Boolean by viewModel.errorPass.observeAsState(initial = false)
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val errorLoadData: Boolean by viewModel.errorLoadData.observeAsState(initial = false)

    var confirmDialog by remember { mutableStateOf(value = false) }
    var cancelDialog by remember { mutableStateOf(value = false) }
    var logOutDialog by remember { mutableStateOf(value = false) }

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
                text = name,
                label = stringResource(R.string.name),
                icon = Icons.Rounded.Person,
                error = errorName,
                errorText = stringResource(input_error_name)
            ) {
                viewModel.onDataInputChange(name = it.trim(), email = email, pass = pass)
            }

            TextFieldCustomDataScreen(
                text = email,
                label = stringResource(R.string.email),
                icon = Icons.Rounded.Email,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email,
                error = errorEmail,
                errorEmailExist = errorEmailExists,
                errorText = stringResource(input_error_email)
            ) {
                viewModel.onDataInputChange(name = name, email = it.trim(), pass = pass)
            }

            TextFieldCustomDataScreen(
                text = pass,
                label = stringResource(password),
                icon = Icons.Rounded.Key,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go, password = true,
                keyboardActions = KeyboardActions(
                    onGo = {
                        confirmDialog = true
                    }
                ),
                error = errorPass,
                errorText = stringResource(input_error_pass)
            ) {
                viewModel.onDataInputChange(name = name, email = email, pass = it.trim())
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonCustomDataScreen(
                text = stringResource(save),
                enable = saveEnabled,
                primary = true,
                error = error
            ) {
                confirmDialog = true
            }
            ButtonCustomDataScreen(text = stringResource(cancel), enable = saveEnabled) {
                cancelDialog = true
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            ButtonCustomDataScreen(text = stringResource(logout), tertiary = true) {
                logOutDialog = true
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
        logOutDialog = false
        viewModel.logOut().also {
            viewModel.viewModelScope.launch {
                it.join()
                navController.navigate(AppScreensRoutes.SplashScreen.route) {
                    navController.backQueue.clear()
                }
            }
        }
    }

    var showSnackBarErrors by remember { mutableStateOf(value = false) }

    if (confirmDialog) UserDataDialog(
        title = stringResource(save_user),
        text = stringResource(sure_about_making_changes),
        confirmButtonText = stringResource(yes_modify_it),
        onDismissRequest = { confirmDialog = false },
        onDismissButtonClick = { confirmDialog = false }
    ) {
        viewModel.onUpdateDataSend(name = name, email = email, pass = pass, image = image)
        confirmDialog = false
        if (error) showSnackBarErrors = true
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


    if (!error) showSnackBarErrors = false
    if (showSnackBarErrors) SnackBarError {
        showSnackBarErrors = false
    }

    if (errorLoadData) Toast.makeText(
        LocalContext.current,
        "Error al cargar los datos de usuario",
        Toast.LENGTH_SHORT
    ).show().let {
        viewModel.resetErrorLoadData()
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
        size = 100.dp
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
    text: String,
    enable: Boolean = true,
    primary: Boolean = false,
    tertiary: Boolean = false,
    error: Boolean = false,
    onclick: () -> Unit
) {
    ButtonCustom(
        text = text,
        enable = enable,
        tertiary = tertiary,
        primary = primary,
        error = error
    ) {
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
    error: Boolean,
    errorEmailExist: Boolean = false,
    errorText: String,
    onTextFieldChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
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
            isError = error || errorEmailExist,
            password = password,
            onTextFieldChanged = { onTextFieldChanged(it) }
        )
        if (error || errorEmailExist) IconError(
            errorText =
            if (errorEmailExist) stringResource(error_email_exist)
            else errorText
        )
    }
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