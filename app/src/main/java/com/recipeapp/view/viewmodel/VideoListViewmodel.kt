package com.recipeapp.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class VideoListViewmodel(initalState: RecipeVideoState,savedStateHandle: SavedStateHandle) :
    BaseMVIViewmodel<RecipeVideoState>(initalState,savedStateHandle) {
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
            setStateAndPersist {
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
        setStateAndPersist {
            copy(event = RecipeVideoEvent.OnRecipeInitialLoad(responses.videos))
        }
    }

    private fun handleResponseFailure(failure: Failure) {

    }
}


sealed class RecipeVideoEvent : Parcelable {
    @Parcelize
    object Uninitialized : RecipeVideoEvent()
    @Parcelize
    data class OnLoad(var isPaginate: Boolean = false, var isLoading: Boolean) : RecipeVideoEvent()
    @Parcelize
    data class OnRecipeInitialLoad(var data: List<Video>) : RecipeVideoEvent()
    @Parcelize
    data class OnError(var isPaginate: Boolean = false, var failure: Failure) : RecipeVideoEvent()
    @Parcelize
    data class onRecipePaginate(var data: Resource<*>) : RecipeVideoEvent()
}

@Parcelize
data class RecipeVideoState(
    var event: RecipeVideoEvent = RecipeVideoEvent.Uninitialized,
    var sideEffect: Consumable<SideEffect>? = null
) : RecipeState(),Parcelable