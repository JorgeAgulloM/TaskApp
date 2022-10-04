package com.softyorch.taskapp.data.network.pexels

import android.util.Log
import com.softyorch.taskapp.data.network.pexels.responseMyCollection.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class PexelsService @Inject constructor(private val retrofit: Retrofit) {

    suspend fun getMediaList(): List<Media> =
        withContext(Dispatchers.IO) {
            retrofit.create(PexelsClient::class.java).getPhotoCollections().let { response ->
                if (response.body() != null) {
                    Log.d("PHOTOS", "Service.getCollections -> ${response.body()!!.media.size}")
                    response.body()!!.media
                } else {
                    emptyList()
                }
            }
        }

}