package com.shared.recipe.usecases

import com.shared.recipe.RecipeEntity
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.repository.LocalRepository
import com.shared.recipe.resource.Either

class SaveRecipeUsecase(val localRepository: LocalRepository) : BaseUsecase<Int,SaveRecipeUsecase.Params>() {

    data class Params(val recipeEntity: RecipeEntity)

    override suspend fun run(params: Params): Either<ApiFailure, Int> {
        localRepository.saveRecipe(params.recipeEntity)
        return Either.Right(0)
    }
}