package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.data.network.pexels.response.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class PexelsService @Inject constructor(private val retrofit: Retrofit) {
    suspend fun getMediaList(): List<Media?> {
        return withContext(Dispatchers.IO) {
            try {
                retrofit.create(PexelsClient::class.java)
                    .getPhotoCollections().body()?.media ?: emptyList()
            } catch (ex: Exception) {
                emptyList()
            }
        }
    }
}