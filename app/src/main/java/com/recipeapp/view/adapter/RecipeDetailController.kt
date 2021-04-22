package com.recipeapp.view.adapter

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import coil.api.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.recipeapp.R
import com.recipeapp.core.platform.BaseController
import com.recipeapp.core.platform.BaseHolder
import com.recipeapp.core.platform.BaseItemModel
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.view.viewholders.LoadingViewHolder_
import com.recipeapp.view.viewmodel.RecipeDetailState


class RecipeDetailController : BaseController() {
    var isLoading = false
    val list = mutableListOf<RecipeDetailItem>()

    override fun buildModels() {
        LoadingViewHolder_().id(111111111111).addIf(isLoading, this)
        list.forEachIndexed { index, it ->
            when (it) {
                is RecipeImage -> ImageViewHolder_().id(index)
                    .imageUrl(it.imageUrl)
                    .addTo(this)
                is RecipeDesc -> RecipeDetailHeader_().id(index)
                    .recipeAuthor(it.recipeSource)
                    .recipeName(it.recipeName).addTo(this)
                is RecipeAbout -> RecipeAboutModel_().id(index)
                    .recipeAbout(it.recipeAbout).addTo(this)
            }
        }
    }

    fun buildModel(state: RecipeDetailState) {
        isLoading = state.isLoading
        state.recipeDetail?.let {
            addRecipeImage(it)
            addRecipeDesc(it)
            addRecipeAbout(it)
//            addRecipeInflammatoryInfo(it)
        }
        requestModelBuild()
    }

    fun addRecipeAbout(recipeDetail: RecipeDetailResponse) {
        RecipeAbout(recipeDetail.instructions).also {
            list.add(it)
        }
    }

    fun addRecipeImage(recipeDetail: RecipeDetailResponse) {
        RecipeImage(recipeDetail.image).also {
            list.add(it)
        }
    }

    fun addRecipeDesc(recipeDetail: RecipeDetailResponse) {
        RecipeDesc(recipeName = recipeDetail.title, recipeSource = recipeDetail.sourceName).also {
            list.add(it)
        }
    }

    fun addRecipeInflammatoryInfo(recipeDetail: RecipeDetailResponse) {
        with(recipeDetail) {
            InflammatoryInfo(glutenFree = this.glutenFree, diaryFree = this.dairyFree).also {
                list.add(it)
            }
        }
    }
}

interface RecipeDetailItem

data class RecipeDesc(
    val recipeName: String,
    val recipeSource: String,
    var isSaved: Boolean = false
) : RecipeDetailItem

data class InflammatoryInfo(val glutenFree: Boolean, val diaryFree: Boolean) : RecipeDetailItem

data class RecipeImage(val imageUrl: String) : RecipeDetailItem

data class RecipeAbout(val recipeAbout: String) : RecipeDetailItem


@EpoxyModelClass(layout = R.layout.item_recipe_image)
abstract class ImageViewHolder : BaseItemModel<ImageHolder>() {

    @EpoxyAttribute
    lateinit var imageUrl: String

    override fun bind(holder: ImageHolder) {
        holder.imvThumbnail.load(imageUrl)
    }
}


class ImageHolder : BaseHolder() {
    val imvThumbnail by bind<ImageView>(R.id.imvThumbnail)
}


@EpoxyModelClass(layout = R.layout.item_recipe_detail_header)
abstract class RecipeDetailHeader : BaseItemModel<RecipeDetailHeaderHolder>() {

    @EpoxyAttribute
    lateinit var recipeName: String

    @EpoxyAttribute
    lateinit var recipeAuthor: String

    override fun bind(holder: RecipeDetailHeaderHolder) {
        holder.recipeName.setText(recipeName)
        holder.recipeAuthor.setText(recipeAuthor)
    }
}


class RecipeDetailHeaderHolder : BaseHolder() {
    val recipeName by bind<AppCompatTextView>(R.id.tvRecipeName)
    val recipeAuthor by bind<AppCompatTextView>(R.id.tvAuthorName)
}

@EpoxyModelClass(layout = R.layout.item_recipe_about)
abstract class RecipeAboutModel : BaseItemModel<RecipeAboutHolder>() {

    @EpoxyAttribute
    lateinit var recipeAbout: String

    override fun bind(holder: RecipeAboutHolder) {
        holder.recipeDetail.setText(recipeAbout)
    }
}


class RecipeAboutHolder : BaseHolder() {
    val recipeDetail by bind<AppCompatTextView>(R.id.tvRecipeAbout)
}


