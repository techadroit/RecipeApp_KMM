package com.recipeapp.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.recipeapp.core.Resource
import com.recipeapp.core.collectIn
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.core.platform.ViewState
import com.recipeapp.core.usecase.FlowUseCase
import com.recipeapp.domain.usecases.LoadSavedRecipeUsecase
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.coroutines.flow.catch

class FragmentShowSavedRecipesViewmodel(initalState: SaveRecipeState) :
    BaseMVIViewmodel<SaveRecipeState>(initalState) {

    val liveData = MutableLiveData<ViewState>()
    lateinit var usecase: LoadSavedRecipeUsecase

    fun loadSavedRecipes() {
        setState {
            copy(event = Resource.Loading)
        }
        usecase(FlowUseCase.None()).catch {
            handleRecipesFailure(this as Failure)
        }.collectIn(viewModelScope) {
            handleOnSuccessForLoadingRecipesFromDatabase(it)
        }
    }

    private fun handleRecipesFailure(failure: Failure) {
        setState {
            copy(event = Resource.Error(data = null, error = failure))
        }
    }

    private fun handleOnSuccessForLoadingRecipesFromDatabase(list: List<RecipeModel>) {
        setState {
            copy(event = Resource.Success(list))
        }
    }
}

data class SaveRecipeState(val event: Resource<Any>? = Resource.Uninitialized) : RecipeState()