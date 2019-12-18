package com.recipeapp.data.repositories

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.repository.BaseRepository
import com.recipeapp.data.network.response.RandomRecipesResponse
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.network.response.VideoListResponses

class RecipeRepository(val recipeApiService: RecipeApi) : BaseRepository {


    suspend fun getRandomRecipe(
        limitLicense: Boolean,
        tags: String,
        number: Int
    ): Either<Failure, RandomRecipesResponse> =
        try {
            val response = recipeApiService.getRandomRecipes(limitLicense, tags, number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

    suspend fun searchRecipeFor(
        query: String,
        limitLicense: Boolean,
        number: Int
    ): Either<Failure, RecipeSearchResponse> =
        try {
            val response = recipeApiService.searchRecipes(limitLicense, query, number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

    suspend fun searchVideoRecipeFor(
        query: String,
        number: Int
    ): Either<Failure, VideoListResponses> =
        try {
            val response = recipeApiService.searchVideos(tags = query,number =  number)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

    suspend fun getRecipeDetailForId(id: String,includeNutrition:Boolean): Either<Failure, RecipeDetailResponse> =
        try {
            val response = recipeApiService.recipeDetail(id = id,includeNutrition = includeNutrition)
            Either.Right(response)
        } catch (e: Exception) {
            Either.Left(Failure.ServerError)
        }

}