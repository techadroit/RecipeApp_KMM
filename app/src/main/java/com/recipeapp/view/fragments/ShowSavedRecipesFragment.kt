package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.recipeapp.R
import com.recipeapp.core.platform.BaseFragment
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewmodel.FragmentShowSavedRecipesViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_list_layout.*

class ShowSavedRecipesFragment : BaseFragment() {
    override fun layoutId(): Int {
        return R.layout.fragment_saved_recipes
    }

    val viewmodel by lazy {
        ViewModelProviders.of(this).get(FragmentShowSavedRecipesViewmodel::class.java)
    }
    val recipeController = RecipeController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpoxy()
        initViewModel()
        observeChanges()
    }

    fun initViewModel() {
        viewmodel.loadSavedRecipes()
    }

    fun initEpoxy() {
        rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
            }
        }
    }

    fun observeChanges() {
        viewmodel.liveData.observe(this, Observer {
            when (it) {
                is ViewState.onSuccess -> {
                    addRecipes(it.data as List<RecipeModel>)
                }
                is ViewState.onLoading -> {
                    if (it.loading) showLoading() else hideLoading()
                }
                else -> {
                }
            }
        }
        )
    }

    fun showLoading() {
        recipeController.isLoading = true
        recipeController.requestModelBuild()
    }

    fun hideLoading() {
        recipeController.isLoading = false
        recipeController.requestModelBuild()
    }

    fun addRecipes(list: List<RecipeModel>) {
        recipeController.list.addAll(list)
        recipeController.requestModelBuild()
    }

}