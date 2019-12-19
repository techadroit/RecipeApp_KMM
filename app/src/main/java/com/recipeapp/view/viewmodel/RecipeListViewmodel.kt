package com.recipeapp.view.viewmodel

import com.recipeapp.core.Consumable
import com.recipeapp.core.Resource
import com.recipeapp.core.asConsumable
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.Empty
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SaveRecipeUsecase
import com.recipeapp.domain.usecases.SearchRecipeUsecase
import com.recipeapp.util.QUERY
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.coroutines.launch

class RecipeListViewmodel(initialState: RecipeListState) :
    BaseMVIViewmodel<RecipeListState>(initialState) {

    lateinit var localRepository: RecipeLocalRepository

    fun saveRecipe(recipeModel: RecipeModel) {
        viewModelScope.launch {
            val usecase = SaveRecipeUsecase(localRepository)
            usecase(SaveRecipeUsecase.Param(recipeModel), {
                onRecipeSaved()
            })
        }
    }

    fun onRecipeSaved() {
        setState {
            copy(onSavedRecipes = Resource.Success(data = Empty))
        }
    }

    fun loadRecipes() = viewModelScope.launch {
        setState { copy(recipes = Resource.Loading.asConsumable()) }
        // TODO pass it as dependency
        val repos =
            RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
        val usecase = SearchRecipeUsecase(repos)
        usecase(SearchRecipeUsecase.Param(query = QUERY),
            { it.either(::handleRecipeResponseFailure, ::handleRecipeSearch) })
    }

    fun handleRecipeResponseFailure(failure: Failure) {
        println(failure)
    }

    fun handleRecipeSearch(response: RecipeSearchResponse) {
        val recipeList = response.results.map {
            RecipeModel(
                it.id,
                it.title,
                it.servings,
                response.baseUri + it.image,
                it.readyInMinutes
            )
        }
        setState {
            copy(recipes = Resource.Success(data = recipeList).asConsumable())
        }
    }

}

data class RecipeListState(
    val recipes: Consumable<Resource<List<RecipeModel>>> = Resource.Uninitialized.asConsumable(),
    val onSavedRecipes: Resource<Empty>? = Resource.Uninitialized
) : RecipeState() {

    val isLoading: Boolean
        get() {
            return (recipes == Resource.Loading.asConsumable())
        }
}