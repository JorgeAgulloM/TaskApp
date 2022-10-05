package com.softyorch.taskapp.data.repository.pexels.model

import com.softyorch.taskapp.data.network.pexels.response.Media

data class MediaModel(
    val imageOriginalSrc: String,
    val imageUrl: String,
    val photographer: String,
    val photographerUrl: String,
)

fun Media.mapToMediaModel() = MediaModel(
    imageOriginalSrc = src.original,
    imageUrl = url,
    photographer = photographer,
    photographerUrl = photographerUrl
)