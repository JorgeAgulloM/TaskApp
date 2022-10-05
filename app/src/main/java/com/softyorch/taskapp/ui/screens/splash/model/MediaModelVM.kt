package com.softyorch.taskapp.ui.screens.splash.model

import com.softyorch.taskapp.domain.pexelUseCase.MediaModelDomain

data class MediaModelVM(
    val image: String,
    val imageUrl: String,
    val author: String,
    val authorUrl: String,
)

fun MediaModelDomain.mapToMediaModelVM() = MediaModelVM(
    image = imageOriginalSrc,
    imageUrl = imageUrl,
    author = photographer,
    authorUrl = photographerUrl
)
