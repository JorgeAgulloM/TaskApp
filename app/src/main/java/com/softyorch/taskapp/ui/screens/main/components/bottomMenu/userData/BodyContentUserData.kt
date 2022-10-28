/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screens.main.components.bottomMenu.userData

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.components.ButtonCustom
import com.softyorch.taskapp.ui.components.CircularIndicatorCustom
import com.softyorch.taskapp.ui.components.SnackBarError
import com.softyorch.taskapp.ui.components.SpacerCustom
import com.softyorch.taskapp.ui.models.AccountModel
import com.softyorch.taskapp.ui.navigation.AppScreensRoutes
import com.softyorch.taskapp.ui.screens.commonErrors.model.ErrorAccountModel
import com.softyorch.taskapp.ui.screens.login.components.TextFieldEmail
import com.softyorch.taskapp.ui.screens.login.components.TextFieldName
import com.softyorch.taskapp.ui.screens.login.components.TextFieldPass
import com.softyorch.taskapp.utils.extensions.toastError
import kotlinx.coroutines.launch

@Composable
fun BodyContentUserData(
    navController: NavController
) {
    val viewModel = hiltViewModel<UserDataViewModel>()
    val userData: AccountModel by viewModel.userDataEntityActive.observeAsState(
        initial = AccountModel.accountModel
    )
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)

    if (isLoading) CircularIndicatorCustom(text = stringResource(R.string.loading_loading))

    val saveEnabled: Boolean by viewModel.saveEnabled.observeAsState(initial = false)

    /** Error states */
    val errors: ErrorAccountModel by viewModel.errorsAccount.observeAsState(
        initial = ErrorAccountModel.errorAccountModel
    )

    val showErrorLoad: Boolean by viewModel.showErrorLoadData.observeAsState(initial = false)

    var confirmDialog by remember { mutableStateOf(value = false) }
    var logOutDialog by remember { mutableStateOf(value = false) }

    SpacerCustom(bottom = 24.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {

        TextFieldName(
            name = userData.userName,
            error = errors.name
        ) {
            viewModel.onDataInputChange(userData.copy(userName = it.trim()))
        }

        TextFieldEmail(
            email = userData.userEmail,
            error = errors.email,
            errorEmailExist = errors.emailExists
        ) {
            viewModel.onDataInputChange(userData.copy(userEmail = it.trim()))
        }

        TextFieldPass(
            pass = userData.userPass,
            keyboardActions = KeyboardActions(
                onGo = {
                    confirmDialog = true
                }
            ),
            error = errors.pass
        ) {
            viewModel.onDataInputChange(userData.copy(userPass = it.trim()))
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
            ButtonCustom(
                text = stringResource(R.string.save),
                enable = saveEnabled || errors.error,
                primary = true,
                error = errors.error
            ) {
                confirmDialog = true
            }
            ButtonCustom(text = stringResource(R.string.logout), tertiary = true) {
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

    if (confirmDialog) UserDataConfirmDialog(
        userData = userData,
        errorEmailRepeat = errors.emailRepeat,
        errorPassRepeat = errors.passRepeat,
        onDismissRequest = { confirmDialog = false },
        onDismissButtonClick = { confirmDialog = false },
        onConfirmButtonClick = { userConfirmed ->
            viewModel.onUpdateDataSend(userConfirmed).let { error ->
                if (!error) {
                    confirmDialog = false
                } else {
                    showSnackBarErrors = true
                }
            }
        }
    )

    if (!errors.error) showSnackBarErrors = false
    if (showSnackBarErrors) SnackBarError {
        showSnackBarErrors = false
    }

    if (showErrorLoad) LocalContext.current
        .toastError("Error al cargar los datos de usuario") {
            viewModel.resetShowErrorLoad()
        }

}