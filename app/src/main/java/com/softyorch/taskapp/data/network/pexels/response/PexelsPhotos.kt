package com.softyorch.taskapp.data.network.pexels.response

import com.google.gson.annotations.SerializedName

data class PexelsPhotos(
    @SerializedName("next_page") val nextPage: String,
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("photos") val photos: List<Photo>,
    @SerializedName("total_results") val totalResults: Int
)