package com.recipeapp.view.adapter

import com.recipeapp.core.platform.BaseController
import com.recipeapp.core.platform.OnClick
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.data.network.response.Video
import com.recipeapp.view.viewholders.LoadingViewHolder_
import com.recipeapp.view.viewholders.RecipeVideoHolders_

class RecipesVideoController : BaseController() {

    val list = mutableListOf<Video>()
    var isLoading = true
    //Todo change it to a appropriate method
    var click: ((event: OnClickEvent?) -> Unit)? = null

    override fun buildModels() {

        LoadingViewHolder_().id("loading").addIf(isLoading, this)
        list.forEachIndexed { index, it ->

            RecipeVideoHolders_().id(it.youTubeId)
                .listener(object : OnClick {
                    override fun onClick(event: OnClickEvent) {
                        click?.invoke(event)
                    }
                })
                .imageUrl(it.thumbnail)
                .title(it.title).addTo(this)
        }
    }
}