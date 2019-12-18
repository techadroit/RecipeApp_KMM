package com.recipeapp.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * from saved_recipe ORDER BY id ASC")
    suspend fun getAllSavedRecipe(): List<SavedRecipe>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: SavedRecipe) : Long
}