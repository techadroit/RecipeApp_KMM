package com.recipeapp.view.viewholders

import android.view.View
import com.airbnb.epoxy.EpoxyModelClass
import com.recipeapp.R
import com.recipeapp.core.platform.BaseHolder
import com.recipeapp.core.platform.BaseItemModel

@EpoxyModelClass(layout = R.layout.item_loading)
abstract class LoadingViewHolder : BaseItemModel<LoadingViewHolder.LoadingEpoxyHolder>() {

    class LoadingEpoxyHolder : BaseHolder() {
        override fun bindView(itemView: View) {

        }
    }
}

@EpoxyModelClass(layout = R.layout.item_page_load)
abstract class PaginationLoadingHolder :
    BaseItemModel<PaginationLoadingHolder.PaginationEpoxyHolder>() {
    class PaginationEpoxyHolder : BaseHolder()
}