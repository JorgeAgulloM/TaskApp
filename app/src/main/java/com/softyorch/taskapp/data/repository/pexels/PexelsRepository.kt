package com.softyorch.taskapp.data.repository.pexels

import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.repository.pexels.model.MediaModel
import com.softyorch.taskapp.data.repository.pexels.model.mapToMediaModel
import com.softyorch.taskapp.utils.emptyString
import com.softyorch.taskapp.utils.extensions.random

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {
    suspend fun getRandomImage(): MediaModel =
        api.getMediaList().let { list ->
            if (list.isNotEmpty()){
                list[list.size.random()]!!.mapToMediaModel()
            } else {
                emptyMedia
            }
        }

    companion object {
        private const val errorImageUrl = "https://www.pexels.com/photo/white-notebook-in-close-up-photography-5717421/"
        private const val errorAuthor = "Polina Kovaleva"
        private const val errorAuthorUrl = "https://www.pexels.com/@polina-kovaleva/"
        private val emptyMedia = MediaModel(
            imageOriginalSrc = emptyString,
            imageUrl = errorImageUrl,
            photographer = errorAuthor,
            photographerUrl = errorAuthorUrl
        )
    }

}