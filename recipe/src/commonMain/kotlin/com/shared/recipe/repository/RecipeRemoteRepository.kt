package com.shared.recipe.repository

import com.shared.recipe.network.ApiService
import com.shared.recipe.network.NetworkClient
import com.shared.recipe.resource.Either
import com.shared.recipe.response.RecipeResponse
import com.shared.recipe.response.VideoRecipeResponse

class RecipeRemoteRepository {
    val limitLicense = false
    val number = 10
    val apiService = ApiService(NetworkClient)


    suspend fun searchRecipeFor(
        query: String,
        limitLicense: Boolean,
        number: Int,
    ): Either<ApiFailure, RecipeResponse> =
        try {
            val response = apiService.getRecipes(limitLicense,query,number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(ApiFailure())
        }

    suspend fun searchVideoRecipeFor(
        query: String,
        number: Int,
    ): Either<ApiFailure, VideoRecipeResponse> =
        try {
            val response = apiService.getRecipesVideos(tags = query,number =  number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(ApiFailure())
        }


}