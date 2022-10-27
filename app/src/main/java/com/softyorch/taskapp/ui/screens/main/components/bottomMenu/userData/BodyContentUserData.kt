/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

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
import com.softyorch.taskapp.utils.extensions.toastError
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContentUserData(
    navController: NavController
) {
    val viewModel = hiltViewModel<UserDataViewModel>()
    val userData by viewModel.userDataEntityActive.observeAsState()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    if (isLoading) CircularIndicatorCustom(text = stringResource(R.string.loading_loading))

    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)

    /** Error states */
    val errorName: Boolean by viewModel.errorName.observeAsState(initial = false)
    val errorEmail: Boolean by viewModel.errorEmail.observeAsState(initial = false)
    val errorEmailExists: Boolean by viewModel.errorEmailExists.observeAsState(initial = false)
    val errorPass: Boolean by viewModel.errorPass.observeAsState(initial = false)
    val error: Boolean by viewModel.error.observeAsState(initial = false)
    val errorLoadData: Boolean by viewModel.errorLoadData.observeAsState(initial = false)

    var confirmDialog by remember { mutableStateOf(value = false) }
    var logOutDialog by remember { mutableStateOf(value = false) }

    SpacerCustom(bottom = 24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {

        TextFieldCustomDataScreen(
            text = userData?.username ?: emptyString,
            label = stringResource(R.string.name),
            icon = Icons.Rounded.Person,
            error = errorName,
            errorText = stringResource(R.string.input_error_name)
        ) {
            viewModel.onDataInputChange(userData!!.copy(username = it.trim()))
        }

        TextFieldCustomDataScreen(
            text = userData?.userEmail ?: emptyString,
            label = stringResource(R.string.email),
            icon = Icons.Rounded.Email,
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Email,
            error = errorEmail,
            errorEmailExist = errorEmailExists,
            errorText = stringResource(R.string.input_error_email)
        ) {
            viewModel.onDataInputChange(userData!!.copy(userEmail = it.trim()))
        }

        TextFieldCustomDataScreen(
            text = userData?.userPass ?: emptyString,
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
            viewModel.onDataInputChange(userData!!.copy(userPass = it.trim()))
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
        viewModel.onUpdateDataSend(userData!!)
        confirmDialog = false
        if (error) showSnackBarErrors = true
    }

    if (!error) showSnackBarErrors = false
    if (showSnackBarErrors) SnackBarError {
        showSnackBarErrors = false
    }

    if (errorLoadData) LocalContext.current
        .toastError("Error al cargar los datos de usuario"){
            viewModel.resetErrorLoadData()
        }

}