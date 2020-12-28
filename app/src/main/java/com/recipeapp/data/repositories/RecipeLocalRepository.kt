package com.recipeapp.data.repositories

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.exception.NoSavedRecipe

import com.recipeapp.core.repository.BaseRepository
import com.recipeapp.data.datasource.RecipeDao
import com.recipeapp.data.datasource.SavedRecipe
import com.recipeapp.view.pojo.RecipeModel
import com.shared.recipe.resource.Either
import kotlinx.coroutines.flow.Flow

class RecipeLocalRepository(var recipeDao: RecipeDao) : BaseRepository {

    suspend fun getAllSavedRecipes(): Either<Failure, List<SavedRecipe>> {
        val list = recipeDao.getAllSavedRecipe()
        if (list.size == 0) {
            return Either.Left(NoSavedRecipe)
        } else {
            return Either.Right(list)
        }
    }

    fun getSavedReicpesCount(): Flow<Long> = recipeDao.getTotalSavedRecipes()
    /**
     * Insert recipe to database
     * @return
     * row_number -> if the insert is sucessfull
     * -1 -> if the recipe already exist
     */
    suspend fun insertRecipe(recipe: SavedRecipe): Either<Failure, Long> {
        val response = recipeDao.insertRecipe(recipe)
        return Either.Right(response)
    }
}

fun mapToRecipeEntity(recipeModel: RecipeModel) = SavedRecipe(
    recipeModel.id,
    recipeModel.imageUrl,
    recipeModel.cookingTime,
    recipeModel.servings,
    recipeModel.title
)
