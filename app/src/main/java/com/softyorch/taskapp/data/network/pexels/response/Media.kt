package com.softyorch.taskapp.data.network.pexels.response

import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("avg_color") val avgColor: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("full_res") val fullRes: Any,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("photographer") val photographer: String,
    @SerializedName("photographer_id") val photographerId: Int,
    @SerializedName("photographer_url") val photographerUrl: String,
    @SerializedName("src") val src: Src,
    @SerializedName("tags") val tags: List<Any>,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("user") val user: User,
    @SerializedName("video_files") val videoFiles: List<VideoFile>,
    @SerializedName("video_pictures") val videoPictures: List<VideoPicture>,
    @SerializedName("width") val width: Int
)