package com.recipeapp.view.adapter

import android.content.Context
import com.recipeapp.core.platform.BaseController
import com.recipeapp.core.platform.OnClick
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.data.network.response.Video
import com.recipeapp.view.viewholders.ErrorViewHolder_
import com.recipeapp.view.viewholders.LoadingViewHolder_
import com.recipeapp.view.viewholders.PaginationLoadingHolder_
import com.recipeapp.view.viewholders.RecipeVideoHolders_
import com.recipeapp.view.viewmodel.RecipeEvent
import com.recipeapp.view.viewmodel.RecipeListState
import com.recipeapp.view.viewmodel.RecipeVideoEvent
import com.recipeapp.view.viewmodel.RecipeVideoState

class RecipesVideoController : BaseController() {

    val list = mutableListOf<Video>()
    var isLoading = true
    //Todo change it to a appropriate method
    var click: ((event: OnClickEvent?) -> Unit)? = null
    var isPaginate = false
    var isError = false
    var isPaginateError = false
    var context: Context? = null

    fun setState(state: RecipeVideoState) {
        val event = state.event

        when (event) {
            is RecipeVideoEvent.OnRecipeInitialLoad -> {
                hideLoading()
                val resource = event.data as List<Video>
                addRecipes(resource)
            }
            is RecipeVideoEvent.OnLoad -> {

                isPaginate = event.isPaginate
                if (event.isLoading) showLoading() else hideLoading()
            }
            is RecipeVideoEvent.OnError -> {

            }
            is RecipeVideoEvent.onRecipePaginate -> {
                hideLoading()
                val resource = event.data as List<Video>
                appendRecipes(resource)

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

    private fun addRecipes(l: List<Video>) {
        list.addAll(l)
    }

    private fun appendRecipes(l: List<Video>) {
        list.addAll(list.size, l)
    }

    private fun addRecipesItemModel() {
        list.forEachIndexed { index, it ->

            RecipeVideoHolders_().id(index)
                .listener(object : OnClick {
                    override fun onClick(event: OnClickEvent) {
                        click?.invoke(event)
                    }
                })
                .imageUrl(it.thumbnail)
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