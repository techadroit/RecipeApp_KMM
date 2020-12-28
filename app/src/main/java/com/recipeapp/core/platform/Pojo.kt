package com.recipeapp.core.platform

import com.shared.recipe.repository.ApiFailure

sealed class ViewState {
    data class onLoading(val loading: Boolean) : ViewState()
    data class onSuccess(val data: Any) : ViewState()
    data class onError(val error: ApiFailure) : ViewState()
}