package com.softyorch.taskapp.data.repository

import com.softyorch.taskapp.data.DataOrException
import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {

    //suspend fun getImages(queryParam: String): List<Photo> = api.getImages(queryParam)
    suspend fun getRandomImage(): DataOrException<Media, Boolean, Exception> {
        val dataOrException = DataOrException<Media, Boolean, Exception>()
        dataOrException.loading = true
        try {
            api.getMediaList().data.let { response ->
                if (!response.isNullOrEmpty()){
                    val randomImage = SecureRandom()
                    randomImage.setSeed(randomImage.generateSeed(response.size))
                    val random = randomImage.nextInt(response.size - 1)
                    dataOrException.data = response[random]
                }
            }
            dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.loading = false
            dataOrException.e = e
        }

        return dataOrException
    }
}