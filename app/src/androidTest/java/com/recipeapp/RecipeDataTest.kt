package com.recipeapp

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.recipeapp.data.datasource.RecipeDao
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.datasource.SavedRecipe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDataTest{

    private lateinit var recipeDao : RecipeDao
    private lateinit var database : RecipeDatabase

    @Before
    fun createDatabase(){

        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context,RecipeDatabase::class.java).build()
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
                assert(it == 0L)
            }
        }

    }
}