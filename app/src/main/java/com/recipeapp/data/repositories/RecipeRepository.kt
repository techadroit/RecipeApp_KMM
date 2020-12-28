package com.recipeapp.data.repositories

import com.recipeapp.core.exception.Failure

import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.repository.BaseRepository
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.shared.recipe.network.ApiService
import com.shared.recipe.network.NetworkClient
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.resource.Either
import com.shared.recipe.response.RecipeResponse
import com.shared.recipe.response.VideoRecipeResponse

class RecipeRepository(val recipeApiService: RecipeApi) : BaseRepository {

    val apiService = ApiService(NetworkClient)

    suspend fun getRandomRecipe(
        limitLicense: Boolean,
        tags: String,
        number: Int
    ): Either<ApiFailure, RecipeResponse> =
        try {
            val response = recipeApiService.getRandomRecipes(limitLicense, tags, number)
            apiService.getRecipes(limitLicense, tags, number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(ApiFailure())
        }

    suspend fun searchRecipeFor(
        query: String,
        limitLicense: Boolean,
        number: Int,
        offset: Int = 0
    ): Either<ApiFailure, RecipeResponse> =
        try {
//            val response = recipeApiService.searchRecipes(limitLicense, query, number,offset = offset)
            val response = apiService.getRecipes(limitLicense, query, number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(ApiFailure())
        }

    suspend fun searchVideoRecipeFor(
        query: String,
        number: Int,
        Offset: Int = 0
    ): Either<Failure, VideoRecipeResponse> =
        try {
            val response =
                recipeApiService.searchVideos(tags = query, number = number, offset = Offset)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

    suspend fun getRecipeDetailForId(
        id: String,
        includeNutrition: Boolean
    ): Either<Failure, RecipeDetailResponse> =
        try {
            val response =
                recipeApiService.recipeDetail(id = id, includeNutrition = includeNutrition)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

}