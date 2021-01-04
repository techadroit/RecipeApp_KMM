package com.recipeapp.view.viewholders

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.recipeapp.R
import com.recipeapp.core.platform.BaseHolder
import com.recipeapp.core.platform.BaseItemModel
import com.recipeapp.core.platform.OnClickEvent

@EpoxyModelClass(layout = R.layout.item_video)
abstract class RecipeVideoHolders : BaseItemModel<VideoHolder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var imageUrl: String

    override fun bind(holder: VideoHolder) {
        holder.imvThumbnail.load(imageUrl)
        holder.tvTitle.text = title
        holder.imvThumbnail.setOnClickListener {
            listener?.onClick(OnClickEvent.PlayVideo(id()))
        }
    }
}

class VideoHolder : BaseHolder() {
    val imvThumbnail by bind<ImageView>(R.id.imvThumbnail)
    val tvTitle by bind<TextView>(R.id.tvTitle)
}