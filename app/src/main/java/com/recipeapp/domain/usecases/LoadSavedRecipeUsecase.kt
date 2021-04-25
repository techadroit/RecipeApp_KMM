package com.recipeapp.domain.usecases

import com.recipeapp.core.usecase.FlowUseCase
import com.recipeapp.data.datasource.toRecipeModal
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.pojo.RecipeModel

class LoadSavedRecipeUsecase(var localRepository: RecipeLocalRepository) :
    FlowUseCase<List<RecipeModel>, FlowUseCase.None>() {

    override suspend fun run(params: None): List<RecipeModel> {
        return localRepository.getAllSavedRecipes().map { it.toRecipeModal() }
    }
}