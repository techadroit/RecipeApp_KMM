package com.shared.recipe.response

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json           = Json(JsonConfiguration.Stable)
// val recipeResponse = json.parse(RecipeResponse.serializer(), jsonString)


import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.internal.*

@Serializable
data class RecipeResponse (
    val results: List<Result>,

    @SerialName("baseUri")
    val baseURI: String,

    val offset: Int,
    val number: Int,
    val totalResults: Int,

    @SerialName("processingTimeMs")
    val processingTimeMS: Long,

    val expires: Long,
    val isStale: Boolean
)

@Serializable
data class Result (
    val id: Int,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,

    @SerialName("sourceUrl")
    val sourceURL: String,

    val openLicense: Long,
    val image: String
)
