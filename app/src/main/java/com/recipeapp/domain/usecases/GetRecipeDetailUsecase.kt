package com.recipeapp.domain.usecases

import com.recipeapp.core.usecase.FlowUseCase
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.data.repositories.RecipeRepository

class GetRecipeDetailUsecase(var recipeRepository: RecipeRepository) :
    FlowUseCase<RecipeDetailResponse, GetRecipeDetailUsecase.Param>() {
    override suspend fun run(params: Param): RecipeDetailResponse {
        return recipeRepository.getRecipeDetailForId(params.id, params.includeNutrition)
    }

    data class Param(var includeNutrition: Boolean = true, var id: String)
}