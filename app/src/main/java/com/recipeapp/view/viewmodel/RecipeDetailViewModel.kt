package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.platform.BaseViewModel
import com.recipeapp.core.platform.ViewState
import kotlinx.coroutines.launch

class RecipeDetailViewModel : BaseViewModel() {

    val recipeDetailLiveData = MutableLiveData<ViewState>()

    fun getRecipeDetailForId(id: String) {
        viewModelScope.launch {
            showLoading(recipeDetailLiveData)
//            usecase(GetRecipeDetailUsecase.Param(id = id),{
//                hideLoading(recipeDetailLiveData)
//                it.either(::handleFailureResponse, ::handleSuccessResponse)
//            })
        }
    }

    fun handleFailureResponse(failure: Failure) {

    }

//    fun handleSuccessResponse(recipeDetailResponse: RecipeDetailResponse){
//        recipeDetailLiveData.value = ViewState.onSuccess(recipeDetailResponse)
//    }
}