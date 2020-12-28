package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.recipeapp.core.platform.BaseViewModel
import com.recipeapp.core.platform.ViewState
import com.recipeapp.view.pojo.RecipeModel
import com.shared.recipe.RecipeEntity
import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.repository.LocalRepository
import com.shared.recipe.resource.Either
import com.shared.recipe.usecases.BaseUsecase
import com.shared.recipe.usecases.LoadSavedRecipeUsecase
import kotlinx.coroutines.launch

class FragmentShowSavedRecipesViewmodel : BaseViewModel() {

    val liveData = MutableLiveData<ViewState>()
    var usecase = LoadSavedRecipeUsecase(LocalRepository())

    fun loadSavedRecipes() {
        viewModelScope.launch {
            showLoading(liveData)
            usecase(BaseUsecase.None(), ::handleRecipeResponse)
        }
    }

    fun handleRecipeResponse(it: Either<ApiFailure, List<RecipeEntity>>) {
        hideLoading(liveData)
        it.either(::handleRecipesFailure, ::handleOnSuccessForLoadingRecipesFromDatabase)
    }

    fun handleRecipesFailure(failure: ApiFailure) {
        liveData.value = ViewState.onError(failure)
    }

    fun handleOnSuccessForLoadingRecipesFromDatabase(list: List<RecipeEntity>) {
        val newList = list.map {
            RecipeModel(it.id,it.title,it.servings,it.imageUrl,it.cookingTime)
        }
        liveData.value = ViewState.onSuccess(newList)
    }
}

//data class SaveRecipeState(var isLoading: Boolean, ) : RecipeState()