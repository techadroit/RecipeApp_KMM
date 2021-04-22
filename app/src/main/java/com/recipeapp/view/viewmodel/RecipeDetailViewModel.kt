package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.domain.usecases.GetRecipeDetailUsecase
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    initialState: RecipeDetailState = RecipeDetailState()
    , savedStateHandle: SavedStateHandle
) : BaseMVIViewmodel<RecipeDetailState>(initialState, savedStateHandle) {
    lateinit var usecase: GetRecipeDetailUsecase

    fun getRecipeDetailForId(id: String) {
        viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }
            usecase(GetRecipeDetailUsecase.Param(id = id)) {
                it.either(::handleFailureResponse, ::handleSuccessResponse)
            }
        }
    }

    fun handleFailureResponse(failure: Failure) {
        setState {
            copy(isLoading = false, recipeDetail = null)
        }
    }

    fun handleSuccessResponse(recipeDetailResponse: RecipeDetailResponse) {
//        recipeDetailLiveData.value = ViewState.onSuccess(recipeDetailResponse)
        setState {
            copy(isLoading = false, recipeDetail = recipeDetailResponse)
        }
    }
}

data class RecipeDetailState(
    val isLoading: Boolean = false,
    val recipeDetail: RecipeDetailResponse? = null
) :
    RecipeState()