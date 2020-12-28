package com.recipeapp.domain.usecases

import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.repositories.RecipeRepository
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.resource.Either
import com.shared.recipe.response.RecipeResponse
import com.shared.recipe.usecases.BaseUsecase

class GetRandomRecipeUsecase(var recipeRepository: RecipeRepository) :
    BaseUsecase<RecipeResponse, GetRandomRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Either<ApiFailure, RecipeResponse> {
        try {
            val response =
                recipeRepository.getRandomRecipe(params.limitLicense, params.tags, params.number)
            return response
        } catch (e: Exception) {
            return Either.Left(ApiFailure())
        }
    }

    data class Param(var limitLicense: Boolean = true, var tags: String, var number: Int = 10)
}