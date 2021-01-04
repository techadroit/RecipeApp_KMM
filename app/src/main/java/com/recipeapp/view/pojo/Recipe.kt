package com.recipeapp.view.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    val id: Int,
    val title: String,
    val servings: Int,
    val imageUrl: String,
    val cookingTime: Int
): Parcelable