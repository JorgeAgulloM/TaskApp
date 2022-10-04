package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {

    //suspend fun getImages(queryParam: String): List<Photo> = api.getImages(queryParam)
    suspend fun getRandomImage(): Media {
        val response =  api.getMediaList()
        val randomImage = SecureRandom()
        randomImage.setSeed(randomImage.generateSeed(response.size))
        val random = randomImage.nextInt(response.size - 0 + 1) + 0

        return response[random]
    }
}