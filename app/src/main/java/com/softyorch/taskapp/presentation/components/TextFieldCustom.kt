package com.softyorch.taskapp.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import com.softyorch.taskapp.utils.ELEVATION_DP


//TextField V1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldCustom(
    text: String = "",
    label: String = "",
    placeholder: String = "",
    icon: ImageVector,
    contentDescription: String,
    keyboardOptions: KeyboardOptions = KEYBOARD_OPTIONS_CUSTOM,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    newTask: Boolean = false,
    readOnly: Boolean = false,
    isError: Boolean = false,
    password: Boolean = false,
    onTextFieldChanged: (String) -> Unit = {}
): String {
    val personalizedShape: Shape = MaterialTheme.shapes.large.copy(
        //topStart = CornerSize(corner),
        //bottomStart = CornerSize(corner),
        topEnd = if (newTask) MaterialTheme.shapes.large.topEnd else ZeroCornerSize,
        bottomEnd = if (newTask) MaterialTheme.shapes.large.bottomEnd else ZeroCornerSize
    )
    val textChange = rememberSaveable { mutableStateOf(text) }
    var passVisible by rememberSaveable { mutableStateOf(password) }

    TextField(
        value = text,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier
            .padding(
                start = if (newTask) 8.dp else 32.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = if (newTask) 8.dp else 0.dp
            )
            .width(width = if (newTask) 370.dp else 350.dp)
            .height(
                height =
                if (singleLine) TextFieldDefaults.MinHeight else TextFieldDefaults.MinHeight * 2
            )
            .shadow(
                elevation = ELEVATION_DP, shape = personalizedShape
            ),
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = contentDescription) },
        trailingIcon = {
            if (password) {
                val image = if (passVisible)
                    Icons.Rounded.Visibility
                else
                    Icons.Rounded.VisibilityOff

                val description = if (passVisible)
                    stringResource(hide_password)
                else
                    stringResource(show_password)

                IconButton(
                    onClick = {
                        passVisible = !passVisible
                    },
                    content = {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        },
        isError = isError,
        visualTransformation = if (!passVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = 5,
        shape = personalizedShape,
        /*colors = TextFieldDefaults.textFieldColors(
            textColor = LightMode90t,
            placeholderColor = LightMode90t.copy(alpha = 0.4f),
            focusedLabelColor = unfocusedColor,
            unfocusedLabelColor = unfocusedColor,
            unfocusedLeadingIconColor = unfocusedColor,
            focusedLeadingIconColor = focusedColor,
            containerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = focusedColor,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = focusedColor
        )*/
    )

    return textChange.value
}