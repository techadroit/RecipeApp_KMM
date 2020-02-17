package com.recipeapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.recipeapp.AndroidTest
import com.recipeapp.data.datasource.RecipeDao
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.datasource.SavedRecipe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipeDataTest : AndroidTest(){

    private lateinit var recipeDao : RecipeDao
    private lateinit var database : RecipeDatabase

    @Before
    fun createDatabase(){

        val context = context()
        database = Room.inMemoryDatabaseBuilder(context, RecipeDatabase::class.java).build()
        recipeDao = database.recipeDao()
    }

    @After
    fun closeDatabase(){
        database.clearAllTables()
        database.close()
    }

    @Test
    fun testDataFlow(){
        runBlocking {
            recipeDao.insertRecipe(SavedRecipe(1,"",5,5,"chicken"))
            recipeDao.getSavedRecipesCount().collect {
                assert(it == 1L)
            }
        }

    }
}