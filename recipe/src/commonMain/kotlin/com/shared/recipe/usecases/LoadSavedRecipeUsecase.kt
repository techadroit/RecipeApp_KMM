package com.shared.recipe.usecases

import com.shared.recipe.RecipeEntity
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.repository.LocalRepository
import com.shared.recipe.resource.Either

class LoadSavedRecipeUsecase(var localRepository: LocalRepository)
    : BaseUsecase<List<RecipeEntity>,BaseUsecase.None>() {
    override suspend fun run(params: None): Either<ApiFailure, List<RecipeEntity>> {
        val list = localRepository.getAllRecipes()
        return Either.Right(list)
    }
}