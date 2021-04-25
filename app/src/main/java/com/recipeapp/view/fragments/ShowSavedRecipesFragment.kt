package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.R
import com.recipeapp.core.Resource
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.domain.usecases.LoadSavedRecipeUsecase
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewmodel.FragmentShowSavedRecipesViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_list_layout.*

class ShowSavedRecipesFragment : BaseMVIFragment() {
    override fun layoutId(): Int {
        return R.layout.fragment_saved_recipes
    }

    val viewmodel: FragmentShowSavedRecipesViewmodel by fragmentViewModel()
    val recipeController = RecipeController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpoxy()
        initViewModel()
        observeChanges()
    }

    fun initViewModel() {
        val localRepository =
            RecipeLocalRepository(RecipeDatabase.getDatabase(requireContext()).recipeDao())
        with(viewmodel) {
            usecase = LoadSavedRecipeUsecase((localRepository))
            loadSavedRecipes()
        }
    }

    fun initEpoxy() {
        rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
            }
        }
    }

    fun observeChanges() {
        renderState(viewmodel) { state ->
            when (state.event) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    addRecipes(state.event.data as List<RecipeModel>)
                }
                else ->  hideLoading()
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