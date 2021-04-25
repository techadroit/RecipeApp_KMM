package com.recipeapp.domain.usecases

import com.recipeapp.core.usecase.FlowUseCase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.mapToRecipeEntity
import com.recipeapp.view.pojo.RecipeModel

class SaveRecipeUsecase(var localRepository: RecipeLocalRepository) :
    FlowUseCase<Long, SaveRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Long {
        val recipe = mapToRecipeEntity(params.recipeModel)
        return localRepository.insertRecipe(recipe)
    }

    data class Param(var recipeModel: RecipeModel)
}



