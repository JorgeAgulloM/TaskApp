/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.ui.screensBeta.login.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Background(
    pexelsImage: MediaModel
) {
    var showInfo by remember { mutableStateOf(value = false) }
    var isSuccess by remember { mutableStateOf(value = false) }
    var counter by remember { mutableStateOf(value = 0) }
    val scope = rememberCoroutineScope()

    scope.launch {
        delay(5000)
        while (counter < 5 && !isSuccess) {
            counter += 1
            delay(1000)
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

        AsyncImage(
            model = pexelsImage.image,
            contentDescription = stringResource(R.string.pexels_courtesy),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.backgroudempty),
            onSuccess = { isSuccess = true }
        )

        if (isSuccess || counter >= 5) {
            showInfo = true
        }

        if (showInfo) BodyScreen(pexelsImage)
    }
}