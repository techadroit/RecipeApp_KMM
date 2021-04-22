package com.recipeapp.view.adapter

import android.content.Context
import com.airbnb.epoxy.EpoxyModel
import com.recipeapp.core.Resource
import com.recipeapp.core.platform.BaseController
import com.recipeapp.core.platform.OnClick
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewholders.ErrorViewHolder_
import com.recipeapp.view.viewholders.LoadingViewHolder_
import com.recipeapp.view.viewholders.PaginationLoadingHolder_
import com.recipeapp.view.viewholders.RecipeViewHolder_
import com.recipeapp.view.viewmodel.RecipeEvent
import com.recipeapp.view.viewmodel.RecipeListState

class RecipeController : BaseController() {

    val list = mutableListOf<RecipeModel>()
    var isLoading = true
    var isPaginate = false
    var isError = false
    var isPaginateError = false
    //Todo change it to a appropriate method
    var click: ((recipeModel: RecipeModel?) -> Unit)? = null
    var rowClick : ((recipeModel: RecipeModel?) -> Unit) ?= null
    var context: Context? = null

    fun setState(state: RecipeListState) {
        val event = state.event

        when (event) {
            is RecipeEvent.OnRecipeInitialLoad -> {
                isError = false
                hideLoading()
                val resource = event.data as Resource.ListData<RecipeModel>
                addRecipes(resource.data)
            }
            is RecipeEvent.OnLoad -> {
                isError = false
                isPaginate = event.isPaginate
                if (event.isLoading) showLoading() else hideLoading()
            }
            is RecipeEvent.OnError -> {
                hideLoading()
                isError = true
            }
            is RecipeEvent.OnRecipePaginate -> {
                isError = false
                hideLoading()
                val resource = event.data as Resource.ListData<RecipeModel>
                appendRecipes(resource.data)

            }
        }
        requestModelBuild()
    }

    private fun showLoading() {
        isLoading = true
    }

    private fun hideLoading() {
        isLoading = false
    }

    private fun addRecipes(l: List<RecipeModel>) {
        list.addAll(l)
    }

    private fun appendRecipes(l: List<RecipeModel>) {
        /**
         * filter duplicate response from the server
         */
        val newList = l.filter {
            !list.contains(it)
        }
        list.addAll(list.size, newList)
    }

    private fun addRecipesItemModel() {
        list.forEachIndexed { index, it ->
            RecipeViewHolder_().id(it.id)
                .recipeModel(it)
                .listener(object : OnClick {
                    override fun onClick(event: OnClickEvent) {
                        when (event) {
                            is OnClickEvent.OnSaveClick -> {
                                val model = adapter.getModelById(event.id) as EpoxyModel<*>
                                val position = adapter.getModelPosition(model)
                                val recipeModel = list.get(position)
                                click?.invoke(recipeModel)
                            }
                            is OnClickEvent.OnRowClick -> {
                                rowClick?.invoke(event.recipeModel)
                            }
                        }
                    }
                })
                .imageUrl(it.imageUrl)
                .cookingTime(it.cookingTime)
                .servings(it.servings)
                .title(it.title).addTo(this)
        }
    }

    override fun buildModels() {

        LoadingViewHolder_().id("loading").addIf(isLoading, this)
        addRecipesItemModel()
        ErrorViewHolder_().id("error").addIf(isError, this)
        PaginationLoadingHolder_().id("page_load").addIf(isPaginate, this)
    }
}

