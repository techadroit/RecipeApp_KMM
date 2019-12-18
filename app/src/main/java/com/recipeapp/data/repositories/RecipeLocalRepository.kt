package com.recipeapp.data.repositories

import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.repository.BaseRepository
import com.recipeapp.data.datasource.RecipeDao
import com.recipeapp.data.datasource.SavedRecipe
import com.recipeapp.view.pojo.RecipeModel

class RecipeLocalRepository(var recipeDao: RecipeDao) : BaseRepository {

    suspend fun getAllSavedRecipes(): Either<Failure, List<SavedRecipe>> {
        val list = recipeDao.getAllSavedRecipe()
        if (list.size == 0) {
            return Either.Left(Failure.NoSavedRecipe)
        } else {
            return Either.Right(list)
        }
    }

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
