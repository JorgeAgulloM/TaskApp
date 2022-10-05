package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.utils.DataOrError
import com.softyorch.taskapp.data.network.pexels.response.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class PexelsService @Inject constructor(private val retrofit: Retrofit) {
    suspend fun getMediaList(): DataOrError<List<Media>, String> {
        val listResponse = DataOrError<List<Media>, String>()
        listResponse.let { response ->
            withContext(Dispatchers.IO) {
                retrofit.create(PexelsClient::class.java).getPhotoCollections().let { request ->
                    if (request.isSuccessful) {
                        if (request.body() != null) {
                            response.data = request.body()!!.media
                        } else {
                            response.data = emptyList()
                        }
                    } else {
                        response.error = request.message()
                        response.data = emptyList()
                    }
                }
            }
        }

        return listResponse
    }
}