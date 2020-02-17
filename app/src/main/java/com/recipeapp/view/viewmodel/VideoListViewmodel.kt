package com.recipeapp.view.viewmodel

import com.recipeapp.core.Consumable
import com.recipeapp.core.Resource
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.data.network.response.Video
import com.recipeapp.data.network.response.VideoListResponses
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SearchVideoRecipeUsecase
import com.recipeapp.util.QUERY
import kotlinx.coroutines.launch

class VideoListViewmodel(var initalState: RecipeVideoState) :
    BaseMVIViewmodel<RecipeVideoState>(initalState) {
    var page = 0

    fun getVideoRecipe() {
        viewModelScope.launch {
            setState {
                copy(event = RecipeVideoEvent.OnLoad(isLoading = true))
            }
            val repos =
                RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
            val usecase = SearchVideoRecipeUsecase(repos)
            usecase(SearchVideoRecipeUsecase.Param(query = QUERY, offset = page)) {
                it.either(::handleResponseFailure, ::handleVideoResponse)
            }
        }
    }

    fun loadMoreRecipes() {
        if (currentState.event is RecipeVideoEvent.OnLoad)
            return
        page++
        viewModelScope.launch {
            setState {
                copy(event = RecipeVideoEvent.OnLoad(isPaginate = true, isLoading = false))
            }
            val repos =
                RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
            val usecase = SearchVideoRecipeUsecase(repos)
            usecase(SearchVideoRecipeUsecase.Param(query = QUERY, offset = page)) {
                it.either(::handleResponseFailure, ::handleVideoResponse)
            }
        }
    }

    private fun handleVideoResponse(responses: VideoListResponses) {
        setState {
            copy(event = RecipeVideoEvent.OnRecipeInitialLoad(responses.videos))
        }
    }

    private fun handleResponseFailure(failure: Failure) {

    }
}

sealed class RecipeVideoEvent {
    object Uninitialized : RecipeVideoEvent()
    data class OnLoad(var isPaginate: Boolean = false, var isLoading: Boolean) : RecipeVideoEvent()
    data class OnRecipeInitialLoad(var data: List<Video>) : RecipeVideoEvent()
    data class OnError(var isPaginate: Boolean = false, var failure: Failure) : RecipeVideoEvent()
    data class onRecipePaginate(var data: Resource<*>) : RecipeVideoEvent()
}

data class RecipeVideoState(
    var event: RecipeVideoEvent = RecipeVideoEvent.Uninitialized,
    var sideEffect: Consumable<SideEffect>? = null
) : RecipeState()