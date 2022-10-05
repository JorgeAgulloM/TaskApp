package com.softyorch.taskapp.domain.pexelUseCase

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.network.pexels.response.Media
import com.softyorch.taskapp.data.repository.pexels.PexelsRepository

class GetImage(private val repository: PexelsRepository) {
    suspend operator fun invoke(): DataOrError<Media, String> {
        DataOrError.mediaResponse.let { response ->
            try {
                repository.getRandomImage().data?.let { media ->
                    response.data = media
                }
            } catch (ex: Exception) {
                response.error = ex.message.toString()
            }
        }

        return DataOrError.mediaResponse
    }
}