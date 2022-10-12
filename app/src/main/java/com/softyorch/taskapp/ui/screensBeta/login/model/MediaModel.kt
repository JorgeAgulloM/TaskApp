package com.softyorch.taskapp.ui.screensBeta.login.model

import com.softyorch.taskapp.domain.pexelUseCase.MediaModelDomain
import com.softyorch.taskapp.utils.emptyString

data class MediaModel(
    var image: String,
    var imageUrl: String,
    var author: String,
    var authorUrl: String,
) {
    companion object {
        val MediaModelEmpty = MediaModel(
            image = emptyString,
            imageUrl = emptyString,
            author = emptyString,
            authorUrl = emptyString
        )
    }
}

fun MediaModelDomain.mapToMediaModel() = MediaModel(
    image = imageOriginalSrc,
    imageUrl = imageUrl,
    author = photographer,
    authorUrl = photographerUrl
)
