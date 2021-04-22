package com.recipeapp.core.platform

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.recipeapp.view.pojo.RecipeModel

abstract class BaseItemModel<T : BaseHolder> : EpoxyModelWithHolder<T>() {
    @EpoxyAttribute
    var listener: OnClick? = null
}

abstract class BaseHolder : KotlinEpoxyHolder() {

}

interface OnClick {
    fun onClick(event: OnClickEvent)
}

sealed class OnClickEvent() {
    data class OnRowClick(var recipeModel: RecipeModel):OnClickEvent()
    data class OnSaveClick(var id: Long) : OnClickEvent()
    data class PlayVideo(var id: Long) : OnClickEvent()
}