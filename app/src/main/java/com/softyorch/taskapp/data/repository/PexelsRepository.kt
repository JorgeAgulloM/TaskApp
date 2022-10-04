package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.network.pexels.response.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {

    //suspend fun getImages(queryParam: String): List<Photo> = api.getImages(queryParam)
    suspend fun getRandomImage(queryParam: String): Photo =
        api.getImages(queryParam).random()
}