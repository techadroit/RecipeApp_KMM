package com.recipeapp.view.adapter

import com.airbnb.epoxy.EpoxyModel
import com.recipeapp.core.platform.BaseController
import com.recipeapp.core.platform.OnClick
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewholders.LoadingViewHolder_
import com.recipeapp.view.viewholders.RecipeViewHolder_

class RecipeController : BaseController() {

    val list = mutableListOf<RecipeModel>()
    var isLoading = true
    //Todo change it to a appropriate method
    var click: ((recipeModel : RecipeModel?)-> Unit)? = null

    override fun buildModels() {

        LoadingViewHolder_().id("loading").addIf(isLoading, this)
        list.forEachIndexed { index, it ->
            RecipeViewHolder_().id(it.id)
                .listener(object : OnClick{
                    override fun onClick(event: OnClickEvent) {
                     when(event){
                         is OnClickEvent.OnSaveClick ->{
                                val model = adapter.getModelById(event.id) as EpoxyModel<*>
                                val position = adapter.getModelPosition(model)
                                val recipeModel = list.get(position)
                                click?.invoke(recipeModel)
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
}

