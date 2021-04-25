package com.recipeapp.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.recipeapp.core.Consumable
import com.recipeapp.core.Resource
import com.recipeapp.core.collectIn
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.Empty
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SaveRecipeUsecase
import com.recipeapp.domain.usecases.SearchRecipeUsecase
import com.recipeapp.util.QUERY
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.coroutines.flow.catch

class RecipeListViewmodel(initialState: RecipeListState, savedStateHandle: SavedStateHandle) :
    BaseMVIViewmodel<RecipeListState>(initialState) {

    lateinit var localRepository: RecipeLocalRepository
    var page = 1
    private val repos =
        RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
    val searchUsecase by lazy { SearchRecipeUsecase(repos) }
    val usecase by lazy { SaveRecipeUsecase(localRepository) }

    fun saveRecipe(recipeModel: RecipeModel) =
        usecase(SaveRecipeUsecase.Param(recipeModel))
            .collectIn(viewModelScope) {
                onRecipeSaved()
            }


    private fun onRecipeSaved() {
        setState {
            copy(
                onSavedRecipes = Resource.Success(data = Empty),
                sideEffect = Consumable(SideEffect.OnSavedRecipe)
            )
        }
    }

    fun loadRecipes() =
        withState {
            if (it.event !is RecipeEvent.OnLoad) {
                setState { copy(event = RecipeEvent.OnLoad(isLoading = true)) }
                searchRecipe(query = QUERY)
            }
        }

    fun paginate() =
        withState {
            if (it.event !is RecipeEvent.OnLoad) {
                page++
                setState { copy(event = RecipeEvent.OnLoad(isPaginate = true, isLoading = true)) }
                searchRecipe(query = QUERY, isPaginate = true)
            }
        }

    private fun searchRecipe(query: String, isPaginate: Boolean = false) {
        searchUsecase(SearchRecipeUsecase.Param(query = query, offset = page))
            .catch { e ->
                handleFailure(e as Failure, isPaginate = isPaginate)
            }.collectIn(viewModelScope) {
                handleRecipeSearch(it, isPaginate)
            }
    }

    private fun handleFailure(failure: Failure, isPaginate: Boolean = false) {
        setState {
            copy(event = RecipeEvent.OnError(isPaginate = false, failure = failure))
        }
    }

    private fun handleRecipeSearch(recipeList: List<RecipeModel>, isPaginate: Boolean) =
        setState {
            copy(
                event =
                if (isPaginate)
                    RecipeEvent.OnRecipePaginate(Resource.ListData(recipeList))
                else
                    RecipeEvent.OnRecipeInitialLoad(Resource.ListData(recipeList)),
                recipes = RecipeData(recipeList)
            )
        }
}


sealed class RecipeEvent {
    object Uninitialized : RecipeEvent()
    data class OnLoad(var isPaginate: Boolean = false, var isLoading: Boolean) : RecipeEvent()
    data class OnRecipeInitialLoad(var data: Resource<*>) : RecipeEvent()
    data class OnError(var isPaginate: Boolean = false, var failure: Failure) : RecipeEvent()
    data class OnRecipePaginate(var data: Resource<*>) : RecipeEvent()
}

sealed class SideEffect {
    object OnSavedRecipe : SideEffect()
}

data class RecipeData(var allRecipes: List<RecipeModel>)

data class RecipeListState(
    var event: RecipeEvent = RecipeEvent.Uninitialized,
    val onSavedRecipes: Resource<Empty>? = Resource.Uninitialized,
    var recipes: RecipeData = RecipeData(emptyList()),
    var sideEffect: Consumable<SideEffect>? = null
) : RecipeState()