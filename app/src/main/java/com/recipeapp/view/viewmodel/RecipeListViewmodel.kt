package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseViewModel
import com.recipeapp.core.platform.Empty
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SaveRecipeUsecase
import com.recipeapp.domain.usecases.SearchRecipeUsecase
import com.recipeapp.util.QUERY
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.coroutines.launch

class RecipeListViewmodel : BaseViewModel() {

    val liveData = MutableLiveData<ViewState>()
    val saveRecipeLiveData = MutableLiveData<ViewState>()
    lateinit var localRepository: RecipeLocalRepository

    fun saveRecipe(recipeModel: RecipeModel) {
        viewModelScope.launch {
            showLoading(saveRecipeLiveData)
            val usecase = SaveRecipeUsecase(localRepository)
            usecase(SaveRecipeUsecase.Param(recipeModel), {
                hideLoading(saveRecipeLiveData)
                onRecipeSaved(saveRecipeLiveData)
            })
        }
    }

    fun onRecipeSaved(saveRecipeLiveData: MutableLiveData<ViewState>) {
        saveRecipeLiveData.value = ViewState.onSuccess(Empty)
    }

    fun loadRecipes() =
        viewModelScope.launch {
            showLoading(liveData)
            // TODO pass it as dependency
            val repos =
                RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
            val usecase = SearchRecipeUsecase(repos)
            usecase(SearchRecipeUsecase.Param(query = QUERY), {
                hideLoading(liveData)
                it.either(::handleRecipeResponseFailure, ::handleRecipeSearch)
            })
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
        liveData.value = ViewState.onSuccess(recipeList)
    }


}