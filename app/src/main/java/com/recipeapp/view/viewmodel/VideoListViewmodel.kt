package com.recipeapp.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.recipeapp.core.Consumable
import com.recipeapp.core.Resource
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.util.QUERY
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.repository.RecipeRemoteRepository
import com.shared.recipe.response.Video
import com.shared.recipe.response.VideoRecipeResponse
import com.shared.recipe.usecases.SearchVideoRecipeUsecase
import kotlinx.coroutines.launch

class VideoListViewmodel(var initalState: RecipeVideoState, savedStateHandle: SavedStateHandle) :
    BaseMVIViewmodel<RecipeVideoState>(initalState, savedStateHandle) {
    var page = 0

    fun getVideoRecipe() {
        viewModelScope.launch {
            setState {
                copy(event = RecipeVideoEvent.OnLoad(isLoading = true))
            }
            val repos = RecipeRemoteRepository()
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
            setStateAndPersist {
                copy(event = RecipeVideoEvent.OnLoad(isPaginate = true, isLoading = false))
            }
            val repos = RecipeRemoteRepository()
            val usecase = SearchVideoRecipeUsecase(repos)
            usecase(SearchVideoRecipeUsecase.Param(query = QUERY, offset = page)) {
                it.either(::handleResponseFailure, ::handleVideoResponse)
            }
        }
    }

    private fun handleVideoResponse(responses: VideoRecipeResponse) {
        setStateAndPersist {
            copy(event = RecipeVideoEvent.OnRecipeInitialLoad(responses.videos))
        }
    }

    private fun handleResponseFailure(failure: ApiFailure) {

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