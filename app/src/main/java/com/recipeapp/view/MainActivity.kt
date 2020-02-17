package com.recipeapp.view

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.recipeapp.R
import com.recipeapp.core.platform.BaseActivity
import com.recipeapp.core.platform.ResId
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    val fragmentContainerId = ResId(R.id.container)
    val recipeLocalRepository by lazy {
        RecipeLocalRepository(RecipeDatabase.getDatabase(this@MainActivity).recipeDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNav()
        updateSavedRecipesBadge()
    }

    fun updateSavedRecipesBadge() {
        lifecycleScope.launch {
            recipeLocalRepository.getSavedReicpesCount().collect {
                if (it > 0)
                    bottomNav.getOrCreateBadge(R.id.action_favorites).number = it.toInt()
            }
        }
    }

    fun initBottomNav() {

        fun deselectAllTheBottomTabs() {
            bottomNav.menu.forEach {
                it.setCheckable(false)
            }
        }

        fun enableMenu(it: MenuItem) {
            it.setCheckable(true)
        }

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_recipes -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    navigator.showRecipeList(fragmentContainerId)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_favorites -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    navigator.showSavedRecipes(fragmentContainerId)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_videos -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    navigator.showRecipeVideo(fragmentContainerId)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_settings -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        deselectAllTheBottomTabs()
        bottomNav.selectedItemId = R.id.action_recipes
    }

}
