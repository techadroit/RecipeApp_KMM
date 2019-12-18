package com.recipeapp.core.platform

import com.recipeapp.core.exception.Failure

sealed class ViewState {
    data class onLoading(val loading: Boolean) : ViewState()
    data class onSuccess(val data: Any) : ViewState()
    data class onError(val error: Failure) : ViewState()
}