package com.softyorch.taskapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.utils.ANIMATED_ENTER_TEXT_FIELDS
import com.softyorch.taskapp.utils.ANIMATED_EXIT_TEXT_FIELDS
import com.softyorch.taskapp.utils.KEYBOARD_OPTIONS_CUSTOM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun outlinedTextFieldCustom(
    text: String = "",
    label: String = "",
    placeholder: String = "",
    icon: ImageVector,
    contentDescription: String,
    keyboardOptions: KeyboardOptions = KEYBOARD_OPTIONS_CUSTOM,
    keyboardActions: KeyboardActions = KeyboardActions(),
    isError: Boolean = false,
    isVisible: Boolean = true,
    password: Boolean = false,
    onTextFieldChanged: (String) -> Unit = {}
): String {
    val personalizedShape: Shape = MaterialTheme.shapes.large
    val textChange = rememberSaveable { mutableStateOf(text) }
    var passVisible by rememberSaveable { mutableStateOf(password) }
    var visible by remember { mutableStateOf(value = false) }

    rememberCoroutineScope().launch {
        delay(100)
        visible = isVisible
    }
    AnimatedVisibility(
        visible = visible,
        enter = ANIMATED_ENTER_TEXT_FIELDS,
        exit = ANIMATED_EXIT_TEXT_FIELDS
    ) {

        val focusedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        val unFocusedColor = MaterialTheme.colorScheme.secondary
        val textColor = MaterialTheme.colorScheme.onSurface
        val placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant

        OutlinedTextField(
            value = text,
            onValueChange = { onTextFieldChanged(it) },
            modifier = Modifier
                .width(350.dp),
            textStyle = MaterialTheme.typography.bodySmall,
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
                        stringResource(R.string.hide_password)
                    else
                        stringResource(R.string.show_password)

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
            supportingText = { Text("HOLA") },
            isError = isError,
            visualTransformation = if (!passVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 5,
            shape = personalizedShape,
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                placeholderColor = unFocusedColor,
                focusedLabelColor = focusedColor,
                unfocusedLabelColor = unFocusedColor,
                unfocusedLeadingIconColor = unFocusedColor,
                focusedLeadingIconColor = focusedColor,
                containerColor = Color.Transparent,
                focusedIndicatorColor = focusedColor,
                unfocusedIndicatorColor = unFocusedColor,
                cursorColor = focusedColor
            )
        )
    }

    return textChange.value
}
