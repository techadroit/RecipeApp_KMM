package com.shared.recipe.response
// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                = Json(JsonConfiguration.Stable)
// val videoRecipeResponse = json.parse(VideoRecipeResponse.serializer(), jsonString)

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.internal.*

@Serializable
data class VideoRecipeResponse (
    val totalResults: Long,
    val videos: List<Video>
)

@Serializable
data class Video (
    val title: String,
    val shortTitle: String,

    @SerialName("youTubeId")
    val youTubeID: String,

    val rating: Double,
    val views: Long,
    val thumbnail: String,
    val length: Long
)
