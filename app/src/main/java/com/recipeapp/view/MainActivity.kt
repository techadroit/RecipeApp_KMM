package com.recipeapp.view

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.recipeapp.R
import com.recipeapp.core.extension.observe
import com.recipeapp.core.platform.BaseActivity
import com.recipeapp.core.platform.BaseFragment
import com.recipeapp.core.platform.ResId
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect


class MainActivity : BaseActivity() {

    val recipeLocalRepository by lazy {
        RecipeLocalRepository(RecipeDatabase.getDatabase(this@MainActivity).recipeDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resId = ResId(R.id.container)
        initBottomNav()
        updateSavedRecipesBadge()
    }

    fun updateSavedRecipesBadge() {
        lifecycleScope.launchWhenStarted {
            recipeLocalRepository.getSavedRecipesCount().collect {
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
                    navigator.showRecipeList()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_favorites -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    navigator.showSavedRecipes()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_videos -> {
                    deselectAllTheBottomTabs()
                    enableMenu(it)
                    navigator.showRecipeVideo()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        deselectAllTheBottomTabs()
        bottomNav.selectedItemId = R.id.action_recipes
    }

}
