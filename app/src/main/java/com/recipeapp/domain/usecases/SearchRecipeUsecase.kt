package com.recipeapp.domain.usecases

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.usecase.FlowUseCase
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.network.response.VideoListResponses
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.util.NUMBER
import com.recipeapp.view.pojo.RecipeModel

//class SearchRecipeUsecase(var recipeRepository: RecipeRepository) :
//    UseCase<RecipeSearchResponse, SearchRecipeUsecase.Param>() {
//    override suspend fun run(params: Param): Either<Failure, RecipeSearchResponse> {
//        val either = recipeRepository.searchRecipeFor(params.query, params.limitLicense, params.number,params.offset)
//        return either
//    }
//
//    data class Param(
//        var limitLicense: Boolean = true,
//        var query: String,
//        var number: Int = 10,
//        var offset: Int = 0
//    )
//}


class SearchRecipeUsecase(var recipeRepository: RecipeRepository) :
    FlowUseCase<List<RecipeModel>, SearchRecipeUsecase.Param>() {
    override suspend fun run(params: Param): List<RecipeModel> {
        val response =  recipeRepository.searchRecipeFor(params.query,
            params.limitLicense, params.number,params.offset)
        val recipeList = response.results.map {
            RecipeModel(
                it.id,
                it.title,
                it.servings,
                response.baseUri + it.image,
                it.readyInMinutes
            )
        }
        return recipeList
    }

    data class Param(
        var limitLicense: Boolean = true,
        var query: String,
        var number: Int = 10,
        var offset: Int = 0
    )
}



class SearchVideoRecipeUsecase(var recipeRepository: RecipeRepository) :
    FlowUseCase<VideoListResponses, SearchVideoRecipeUsecase.Param>() {
    override suspend fun run(params: Param): VideoListResponses {
        return recipeRepository.searchVideoRecipeFor(params.query, params.number,params.offset)
    }

    data class Param(var query: String, var number: Int = NUMBER,var offset : Int = 0)
}
