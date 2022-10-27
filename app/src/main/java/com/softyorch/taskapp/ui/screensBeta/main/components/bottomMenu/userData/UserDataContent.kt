/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.main.components.bottomMenu.userData

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.SnackBarError
import com.softyorch.taskapp.ui.components.SpacerCustom
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.utils.emptyString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataContent(
    navController: NavController
) {
    val viewModel = hiltViewModel<UserDataViewModel>()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    if (isLoading) CircularIndicatorCustom(text = stringResource(R.string.loading_loading))

    val image: String by viewModel.image.observeAsState(initial = emptyString)
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

    SpacerCustom(bottom = 24.dp)
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
            errorText = stringResource(R.string.input_error_name)
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
            errorText = stringResource(R.string.input_error_email)
        ) {
            viewModel.onDataInputChange(name = name, email = it.trim(), pass = pass)
        }

        TextFieldCustomDataScreen(
            text = pass,
            label = stringResource(R.string.password),
            icon = Icons.Rounded.Key,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go, password = true,
            keyboardActions = KeyboardActions(
                onGo = {
                    confirmDialog = true
                }
            ),
            error = errorPass,
            errorText = stringResource(R.string.input_error_pass)
        ) {
            viewModel.onDataInputChange(name = name, email = email, pass = it.trim())
        }
    }
    SpacerCustom(bottom = 16.dp)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ButtonCustomDataScreen(
                text = stringResource(R.string.save),
                enable = saveEnabled || error,
                primary = true,
                error = error
            ) {
                confirmDialog = true
            }
            ButtonCustomDataScreen(
                text = stringResource(R.string.cancel),
                enable = saveEnabled || error
            ) {
                cancelDialog = true
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            ButtonCustomDataScreen(text = stringResource(R.string.logout), tertiary = true) {
                logOutDialog = true
            }
        }
    }


    if (logOutDialog) UserDataDialog(
        title = stringResource(R.string.logout),
        text = stringResource(R.string.sure_you_want_logout),
        confirmButtonText = stringResource(R.string.yes_sure),
        onDismissRequest = { logOutDialog = false },
        onDismissButtonClick = { logOutDialog = false }
    ) {
        logOutDialog = false
        viewModel.logOut().also {
            viewModel.viewModelScope.launch {
                it.join()
                navController.navigate(AppScreensRoutes.LoginScreenBeta.route) {
                    navController.backQueue.clear()
                }
            }
        }
    }

    var showSnackBarErrors by remember { mutableStateOf(value = false) }

    if (confirmDialog) UserDataDialog(
        title = stringResource(R.string.save_user),
        text = stringResource(R.string.sure_about_making_changes),
        confirmButtonText = stringResource(R.string.yes_modify_it),
        onDismissRequest = { confirmDialog = false },
        onDismissButtonClick = { confirmDialog = false }
    ) {
        viewModel.onUpdateDataSend(name = name, email = email, pass = pass, image = image)
        confirmDialog = false
        if (error) showSnackBarErrors = true
    }

    if (cancelDialog) UserDataDialog(
        title = stringResource(R.string.cancel_change_user),
        text = stringResource(R.string.sure_not_make_changes),
        confirmButtonText = stringResource(R.string.yes_not_make_it),
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