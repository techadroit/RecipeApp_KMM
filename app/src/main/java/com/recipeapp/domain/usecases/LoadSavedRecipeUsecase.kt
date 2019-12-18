package com.recipeapp.domain.usecases

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.functional.flatMap
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.datasource.toRecipeModal
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.pojo.RecipeModel

class LoadSavedRecipeUsecase(var localRepository: RecipeLocalRepository) :
    UseCase<List<RecipeModel>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<RecipeModel>> {
        val response = localRepository.getAllSavedRecipes()
        val mappedReponse = response.flatMap { list ->
            Either.Right(list.map { it.toRecipeModal() }
            )
        }
        return mappedReponse
    }
}