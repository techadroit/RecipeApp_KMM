package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.R
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.GetRecipeDetailUsecase
import com.recipeapp.view.adapter.RecipeDetailController
import com.recipeapp.view.viewmodel.RecipeDetailViewModel
import kotlinx.android.synthetic.main.fragment_recipe_detail.*

class RecipeDetailFragment : BaseMVIFragment() {

    val viewmodel: RecipeDetailViewModel by fragmentViewModel()
    val epoxyController = RecipeDetailController()

    override fun layoutId(): Int {
        return R.layout.fragment_recipe_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewmodel){
            val repository = RecipeRepository(NetworkHandler.getRecipeService())
            usecase = GetRecipeDetailUsecase(repository)
        }
        arguments?.getString("id")?.let {
            viewmodel.getRecipeDetailForId(it)
        }
        rvList.setController(epoxyController)
        renderState(viewmodel) {
            epoxyController.buildModel(it)
        }
    }
}

