package com.recipeapp.view

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.recipeapp.R
import com.recipeapp.core.platform.BaseActivity
import com.recipeapp.core.platform.ResId
import com.recipeapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    val fragmentContainerId by lazy {
        ResId(binding.container.id)
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNav()
        updateSavedRecipesBadge()
    }

    fun updateSavedRecipesBadge() {
        lifecycleScope.launch {
//            recipeLocalRepository.getSavedReicpesCount().collect {
//                if (it > 0)
//                    bottomNav.getOrCreateBadge(R.id.action_favorites).number = it.toInt()
//            }
        }
    }

    fun initBottomNav() {

        fun deselectAllTheBottomTabs() {
            binding.bottomNav.menu.forEach {
                it.setCheckable(false)
            }
        }

        fun enableMenu(it: MenuItem) {
            it.setCheckable(true)
        }

        binding.bottomNav.setOnNavigationItemSelectedListener {
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
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        deselectAllTheBottomTabs()
        binding.bottomNav.selectedItemId = R.id.action_recipes
    }

}
