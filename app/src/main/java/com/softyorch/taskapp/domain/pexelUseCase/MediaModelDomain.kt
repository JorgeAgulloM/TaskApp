package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.data.repository.pexels.model.MediaModel

data class MediaModelDomain(
    val imageOriginalSrc: String,
    val imageUrl: String,
    val photographer: String,
    val photographerUrl: String,
)

fun MediaModel.mapToMediaModelDomain() = MediaModelDomain(
    imageOriginalSrc = imageOriginalSrc,
    imageUrl = imageUrl,
    photographer = photographer,
    photographerUrl = photographerUrl
)