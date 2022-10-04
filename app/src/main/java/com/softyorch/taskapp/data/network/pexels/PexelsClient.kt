package com.softyorch.taskapp.data.network.pexels

import com.softyorch.taskapp.data.network.pexels.responseMyCollection.PexelsMyCollections
import com.softyorch.taskapp.ui.activities.KEY_API_PEXELS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface PexelsClient {

    @Headers("Authorization: $KEY_API_PEXELS")
    @GET("v1/collections/6hyx1za")
    suspend fun getPhotoCollections(
        @Query("per_page") perPageParam: String = "20"
    ): Response<PexelsMyCollections>
}