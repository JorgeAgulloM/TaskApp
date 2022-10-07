package com.softyorch.taskapp.data.network.pexels.response

import com.google.gson.annotations.SerializedName

data class VideoFile(
    @SerializedName("file_type") val fileType: String,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("link") val link: String,
    @SerializedName("quality") val quality: String,
    @SerializedName("width") val width: Int
)