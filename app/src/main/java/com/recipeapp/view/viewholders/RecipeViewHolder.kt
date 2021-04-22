package com.recipeapp.view.viewholders

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import coil.api.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.google.android.material.button.MaterialButton
import com.recipeapp.R
import com.recipeapp.core.platform.BaseHolder
import com.recipeapp.core.platform.BaseItemModel
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.view.pojo.RecipeModel

@EpoxyModelClass(layout = R.layout.item_recipe_search)
abstract class RecipeViewHolder : BaseItemModel<RecipeHolder>() {

    @EpoxyAttribute
    lateinit var title: String
    @EpoxyAttribute
    var cookingTime: Int = 0
    @EpoxyAttribute
    var servings: Int = 0
    @EpoxyAttribute
    lateinit var imageUrl: String
    @EpoxyAttribute
    var recipeModel : RecipeModel ?= null

    override fun bind(holder: RecipeHolder) {
        holder.tvTitle.text = title
        holder.cookingTime.text = cookingTime.toString()
        holder.servings.text = servings.toString()
        holder.imageView.load(imageUrl)
        holder.btnSave.setOnClickListener {
            listener?.onClick(OnClickEvent.OnSaveClick(id()))
        }
        holder.parent.setOnClickListener{
            recipeModel?.let {
                listener?.onClick(OnClickEvent.OnRowClick(it))
            }
        }
    }
}

class RecipeHolder : BaseHolder() {
    val tvTitle by bind<TextView>(R.id.tvTitle)
    val cookingTime by bind<TextView>(R.id.tvCookingTime)
    val servings by bind<TextView>(R.id.tvServings)
    val imageView by bind<ImageView>(R.id.imvRecipe)
    val btnSave by bind<MaterialButton>(R.id.btnSave)
    val parent by bind<ConstraintLayout>(R.id.parent)
}


