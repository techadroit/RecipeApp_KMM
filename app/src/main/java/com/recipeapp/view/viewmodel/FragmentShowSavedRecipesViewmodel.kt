package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.platform.BaseViewModel
import com.recipeapp.core.platform.ViewState
import com.recipeapp.core.usecase.UseCase
import com.recipeapp.domain.usecases.LoadSavedRecipeUsecase
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.coroutines.launch

class FragmentShowSavedRecipesViewmodel : BaseViewModel() {

    val liveData = MutableLiveData<ViewState>()
    lateinit var usecase: LoadSavedRecipeUsecase

    fun loadSavedRecipes() {
        viewModelScope.launch {
            showLoading(liveData)
            usecase(UseCase.None(), ::handleRecipeResponse)
        }
    }

    fun handleRecipeResponse(it: Either<Failure, List<RecipeModel>>) {
        hideLoading(liveData)
        it.either(::handleRecipesFailure, ::handleOnSuccessForLoadingRecipesFromDatabase)
    }

    fun handleRecipesFailure(failure: Failure) {
        liveData.value = ViewState.onError(failure)
    }

    fun handleOnSuccessForLoadingRecipesFromDatabase(list: List<RecipeModel>) {
        liveData.value = ViewState.onSuccess(list)
    }
}

//data class SaveRecipeState(var isLoading: Boolean, ) : RecipeState()