package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {

    //suspend fun getImages(queryParam: String): List<Photo> = api.getImages(queryParam)
    suspend fun getRandomImage(): DataOrError<Media, String> {
        DataOrError.mediaResponse.let { response ->
            try {
                api.getMediaList().data?.let { list ->
                    val randomImage = SecureRandom()
                    randomImage.setSeed(randomImage.generateSeed(list.size))
                    val random = randomImage.nextInt(list.size - 1)
                    response.data = list[random]
                }
            } catch (ex: Exception) {
                response.error = ex.message.toString()
            }
        }

        return DataOrError.mediaResponse
    }
}