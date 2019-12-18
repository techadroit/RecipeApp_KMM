package com.recipeapp.domain.usecases

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.network.response.RandomRecipesResponse
import com.recipeapp.data.repositories.RecipeRepository

class GetRandomRecipeUsecase(var recipeRepository: RecipeRepository) : UseCase<RandomRecipesResponse, GetRandomRecipeUsecase.Param>() {
    override suspend fun run(params: Param): Either<Failure, RandomRecipesResponse> {
        try {
            return recipeRepository.getRandomRecipe(params.limitLicense,params.tags,params.number)
//            return Either.Right(response)
        } catch (e: Exception) {
            return Either.Left(Failure.ServerError)
        }
    }

    data class Param(var limitLicense : Boolean = true,var tags : String,var number : Int = 10)
}