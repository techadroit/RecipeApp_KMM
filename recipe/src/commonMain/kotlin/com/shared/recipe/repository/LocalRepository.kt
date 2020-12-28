package com.shared.recipe.repository

import com.shared.recipe.RecipeDatabase
import com.shared.recipe.RecipeEntity
import org.kodein.db.asModelSequence
import org.kodein.memory.use

class LocalRepository {

    val db = RecipeDatabase.getDatabase()

    suspend fun saveRecipe(recipeEntity: RecipeEntity) {
        db.put(recipeEntity)
    }

    suspend fun getAllRecipes(): List<RecipeEntity> {
        val list = mutableListOf<RecipeEntity>()
        db.find(RecipeEntity::class).all().use {
            while (it.isValid()) {
                val model = it.model()
                list.add(model)
                it.next()
            }
        }
        return list
    }

    suspend fun getSavedRecipesCount(): Int =
        db.find(RecipeEntity::class).all().asModelSequence().count()

}