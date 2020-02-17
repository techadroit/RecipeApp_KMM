package com.recipeapp.view.viewholders

import com.airbnb.epoxy.EpoxyModelClass
import com.recipeapp.R
import com.recipeapp.core.platform.BaseHolder
import com.recipeapp.core.platform.BaseItemModel

@EpoxyModelClass(layout = R.layout.item_error_on_load)
abstract class ErrorViewHolder : BaseItemModel<ErrorViewHolder.ErrorEpoxyHolder>(){

    class ErrorEpoxyHolder : BaseHolder()
}

