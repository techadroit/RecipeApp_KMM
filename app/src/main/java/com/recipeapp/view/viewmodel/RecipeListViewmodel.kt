package com.recipeapp.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.recipeapp.core.Consumable
import com.recipeapp.core.Resource
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.core.platform.BaseMVIViewmodel
import com.recipeapp.core.platform.Empty
import com.recipeapp.core.platform.RecipeState
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.SaveRecipeUsecase
import com.recipeapp.domain.usecases.SearchRecipeUsecase
import com.recipeapp.util.QUERY
import com.recipeapp.view.pojo.RecipeModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.coroutines.launch

class RecipeListViewmodel(initialState: RecipeListState,savedStateHandle: SavedStateHandle) :
    BaseMVIViewmodel<RecipeListState>(initialState,savedStateHandle) {

    lateinit var localRepository: RecipeLocalRepository
    var page = 1
    private val repos =
        RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
    val searchUsecase by lazy { SearchRecipeUsecase(repos) }
    val usecase by lazy { SaveRecipeUsecase(localRepository) }

    fun saveRecipe(recipeModel: RecipeModel) {
        viewModelScope.launch {
            usecase(SaveRecipeUsecase.Param(recipeModel), { onRecipeSaved() })
        }
    }

    private fun onRecipeSaved() {
        setStateAndPersist {
            copy(
                onSavedRecipes = Resource.Success(data = Empty),
                sideEffect = Consumable(SideEffect.OnSavedRecipe)
            )
        }
    }

    fun loadRecipes() = viewModelScope.launch {

        if (currentState.event is RecipeEvent.OnLoad)
            return@launch

        setStateAndPersist { copy(event = RecipeEvent.OnLoad(isLoading = true)) }
        searchUsecase(SearchRecipeUsecase.Param(query = QUERY)) {
            when (it) {
                is Either.Left -> handleFailure(it.a, isPaginate = false)
                is Either.Right -> handleRecipeSearch(it.b)
            }
        }
    }

    fun paginate() = viewModelScope.launch {
        if (currentState.event is RecipeEvent.OnLoad)
            return@launch
        page++
        setState { copy(event = RecipeEvent.OnLoad(isPaginate = true, isLoading = true)) }
        searchUsecase(SearchRecipeUsecase.Param(query = QUERY, offset = page)) {
            when (it) {
                is Either.Left -> handleFailure(it.a, isPaginate = true)
                is Either.Right -> handleRecipePagination(it.b)
            }
        }
    }


    private fun handleFailure(failure: Failure, isPaginate: Boolean = false) {
        setState {
            copy(event = RecipeEvent.OnError(isPaginate = false, failure = failure))
        }
    }

    private fun handleRecipeSearch(response: RecipeSearchResponse) {
        val recipeList = response.results.map {
            RecipeModel(
                it.id,
                it.title,
                it.servings,
                response.baseUri + it.image,
                it.readyInMinutes
            )
        }
        setState {
            copy(
                event = RecipeEvent.OnRecipeInitialLoad(Resource.ListData(recipeList)),
                recipes = RecipeData(recipeList)
            )
        }
    }

    private fun handleRecipePagination(response: RecipeSearchResponse) {
        val recipeList = response.results.map {
            RecipeModel(
                it.id,
                it.title,
                it.servings,
                response.baseUri + it.image,
                it.readyInMinutes
            )
        }
        setState {
            copy(
                event = RecipeEvent.OnRecipePaginate(Resource.ListData(recipeList)),
                recipes = RecipeData(recipeList)
            )
        }
    }

}


sealed class RecipeEvent : Parcelable{
    @Parcelize
    object Uninitialized : RecipeEvent()
    @Parcelize
    data class OnLoad(var isPaginate: Boolean = false, var isLoading: Boolean) : RecipeEvent()
    @Parcelize
    data class OnRecipeInitialLoad(var data: Resource<*>) : RecipeEvent()
    @Parcelize
    data class OnError(var isPaginate: Boolean = false, var failure: Failure) : RecipeEvent()
    @Parcelize
    data class OnRecipePaginate(var data: Resource<*>) : RecipeEvent()
}

sealed class SideEffect {
    object OnSavedRecipe : SideEffect()
}

@Parcelize
data class RecipeData(var allRecipes: List<RecipeModel>) : Parcelable

@Parcelize
data class RecipeListState(
    var event: RecipeEvent = RecipeEvent.Uninitialized,
    val onSavedRecipes: Resource<Empty>? = Resource.Uninitialized,
    var recipes: RecipeData = RecipeData(emptyList()),
    var sideEffect: Consumable<SideEffect> ?= null
) : RecipeState()