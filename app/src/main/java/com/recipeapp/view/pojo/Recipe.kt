package com.recipeapp.view.pojo

data class RecipeModel(
    val id: Int,
    val title: String,
    val servings: Int,
    val imageUrl: String,
    val cookingTime: Int
)