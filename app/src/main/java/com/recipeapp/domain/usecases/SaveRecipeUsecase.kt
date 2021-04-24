package com.recipeapp.domain.usecases

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.mapToRecipeEntity
import com.recipeapp.view.pojo.RecipeModel

class SaveRecipeUsecase(var localRepository: RecipeLocalRepository) :
    UseCase<Long, SaveRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Either<Failure, Long> {
        val recipe = mapToRecipeEntity(params.recipeModel)
        return localRepository.insertRecipe(recipe)
    }

    data class Param(var recipeModel: RecipeModel)
}



