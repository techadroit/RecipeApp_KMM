package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseViewModel
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.network.response.VideoListResponses
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SearchVideoRecipeUsecase
import com.recipeapp.util.QUERY
import kotlinx.coroutines.launch

class VideoListViewmodel : BaseViewModel() {

    val videosListLiveData = MutableLiveData<ViewState>()

    fun getVideoRecipe() {

        viewModelScope.launch {
            showLoading(videosListLiveData)
            val repos =
                RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
            val usecase = SearchVideoRecipeUsecase(repos)
            usecase(SearchVideoRecipeUsecase.Param(query = QUERY), {
                hideLoading(videosListLiveData)
                it.either(::handleResponseFailure,::handleVideoResponse)
            })
        }
    }

    fun handleVideoResponse(responses: VideoListResponses){
        videosListLiveData.value = ViewState.onSuccess(responses.videos)
    }

    fun handleResponseFailure(failure : Failure){

    }
}