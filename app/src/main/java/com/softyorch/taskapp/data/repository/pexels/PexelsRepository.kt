package com.softyorch.taskapp.data.repository.pexels

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.network.pexels.PexelsService
import com.softyorch.taskapp.data.repository.pexels.model.MediaModel
import com.softyorch.taskapp.data.repository.pexels.model.mapToMediaModel
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PexelsRepository @Inject constructor(private val api: PexelsService) {

    suspend fun getRandomImage(): DataOrError<MediaModel, String> {
        val mediaModelResponse = DataOrError<MediaModel, String>()
        mediaModelResponse.let { dataOrError ->
            try {
                val responseApi = api.getMediaList()
                responseApi.let {
                    it.data?.let { list ->
                        val randomImage = SecureRandom()
                        randomImage.setSeed(randomImage.generateSeed(list.size))
                        val random = randomImage.nextInt(list.size - 1)
                        dataOrError.data = list[random].mapToMediaModel()
                    }
                    it.error = dataOrError.error
                }
            } catch (ex: Exception) {
                dataOrError.error = ex.message.toString()
            }
        }

        return mediaModelResponse
    }
}