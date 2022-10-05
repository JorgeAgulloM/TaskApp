package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.repository.pexels.PexelsRepository

class GetImage(private val repository: PexelsRepository) {
    suspend operator fun invoke(): DataOrError<MediaModelDomain, String> {
        val mediaModelDomain = DataOrError<MediaModelDomain, String>()
        mediaModelDomain.let { response ->
            try {
                repository.getRandomImage().data?.let { media ->
                    response.data = media.mapToMediaModelDomain()
                }
            } catch (ex: Exception) {
                response.error = ex.message.toString()
            }
        }

        return mediaModelDomain
    }
}