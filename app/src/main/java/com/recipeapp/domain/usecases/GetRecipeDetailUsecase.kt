package com.recipeapp.domain.usecases

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.data.repositories.RecipeRepository

class GetRecipeDetailUsecase(var recipeRepository: RecipeRepository) :
    UseCase<RecipeDetailResponse, GetRecipeDetailUsecase.Param>() {
    override suspend fun run(params: Param): Either<Failure, RecipeDetailResponse> {
        return recipeRepository.getRecipeDetailForId(params.id, params.includeNutrition)
    }

    data class Param(var includeNutrition: Boolean = true, var id: String)
}