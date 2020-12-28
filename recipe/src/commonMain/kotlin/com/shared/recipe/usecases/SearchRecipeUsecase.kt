package com.shared.recipe.usecases

import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.repository.RecipeRemoteRepository
import com.shared.recipe.resource.Either
import com.shared.recipe.response.RecipeResponse
import com.shared.recipe.response.VideoRecipeResponse

class SearchRecipeUsecase(var recipeRepository: RecipeRemoteRepository) :
    BaseUsecase<RecipeResponse, SearchRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Either<ApiFailure, RecipeResponse> {
        val either = recipeRepository.searchRecipeFor(
            params.query,
            params.limitLicense,
            params.number
        )
        return either
    }

    data class Param(
        var limitLicense: Boolean = true,
        var query: String,
        var number: Int = 10,
        var offset: Int = 0
    )
}

class SearchVideoRecipeUsecase(var recipeRepository: RecipeRemoteRepository) :
    BaseUsecase<VideoRecipeResponse, SearchVideoRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Either<ApiFailure, VideoRecipeResponse> {
        return recipeRepository.searchVideoRecipeFor(params.query,params.number)
    }

    data class Param(var query: String, var number: Int = 10, var offset: Int = 0)
}
