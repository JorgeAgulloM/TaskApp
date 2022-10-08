package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.repository.pexels.PexelsRepository

class GetImage(private val repository: PexelsRepository) {
    suspend operator fun invoke(): DataOrError<MediaModelDomain, String> {
        val mediaModelDomain = DataOrError<MediaModelDomain, String>()
        mediaModelDomain.let { dataOrError ->
            try {
                val response = repository.getRandomImage()
                response.let {
                    it.data?.let { media ->
                        dataOrError.data = media.mapToMediaModelDomain()
                    }
                    it.error = dataOrError.error
                }
            } catch (ex: Exception) {
                dataOrError.error = ex.message.toString()
            }
        }

        return mediaModelDomain
    }
}