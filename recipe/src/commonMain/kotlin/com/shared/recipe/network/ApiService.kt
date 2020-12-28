package com.shared.recipe.network

import com.shared.recipe.response.RecipeResponse
import com.shared.recipe.response.VideoRecipeResponse
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ApiService(private val client: NetworkClient) {

    private val API_KEY = "95aed809c8d84dd6b831b9aaa35c5f24"
    private val BASE_URL: String = "https://api.spoonacular.com/"

    suspend fun getRecipes(limitLicense: Boolean, tags: String, number: Int): RecipeResponse {
        val response =  client.getNetworkClient().get<RecipeResponse> {
            url(BASE_URL + "recipes/search")
            parameter("limitLicense", limitLicense)
            parameter("query", tags)
            parameter("number", number)
            parameter("offset", 0)
            parameter("apiKey", API_KEY)
        }
        return response
    }

    suspend fun getRecipesVideos(tags: String, number: Int): VideoRecipeResponse {
        val response =  client.getNetworkClient().get<VideoRecipeResponse> {
            url(BASE_URL + "food/videos/search")
            parameter("query", tags)
            parameter("number", number)
            parameter("offset", 0)
            parameter("apiKey", API_KEY)
        }
        return response
    }

}