package com.softyorch.taskapp.ui.screensBeta.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softyorch.taskapp.R
import com.softyorch.taskapp.ui.screensBeta.login.model.MediaModel

@Composable
fun BodyScreen(pexelsImage: MediaModel) {
    val pexelsUrl = stringResource(R.string.pexels_web)
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).padding(vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uriHandler = LocalUriHandler.current

        AppTitle()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 40.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.End
        ) {
            DetailsImageFromPexels(text = stringResource(R.string.pexels_courtesy)) {
                uriHandler.openUri(pexelsUrl)
            }
            DetailsImageFromPexels(text = stringResource(R.string.author) + pexelsImage.author) {
                uriHandler.openUri(pexelsImage.authorUrl)
            }
            DetailsImageFromPexels(text = stringResource(R.string.click_to_view)) {
                uriHandler.openUri(pexelsImage.imageUrl)
            }
        }
    }
}