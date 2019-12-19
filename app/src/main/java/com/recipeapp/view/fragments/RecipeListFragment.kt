package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.R
import com.recipeapp.core.Resource
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewmodel.RecipeListViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_list_layout.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RecipeListFragment : BaseMVIFragment() {
    override fun layoutId(): Int {
        return R.layout.fragment_recipe_list_layout
    }

    val viewmodel: RecipeListViewmodel by fragmentViewModel()
    val recipeController = RecipeController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpoxy()
        viewmodel.localRepository =
            RecipeLocalRepository(RecipeDatabase.getDatabase(context!!).recipeDao())
        viewmodel.loadRecipes()
        observeChanges()
    }

    fun initEpoxy() {
        rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
                viewmodel.saveRecipe(it)
            }
        }
    }

    fun observeChanges() {
        viewScope.launch {
            viewmodel.state.collect { state ->
                if (state.isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }

                state.recipes()?.let {
                    if (it is Resource.Success) {
                        val list = it.data
                        addRecipes(list)
                    }
                }
                if (state.onSavedRecipes is Resource.Success) {
                    Toast.makeText(context, "saved successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
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