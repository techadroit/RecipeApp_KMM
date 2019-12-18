package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.recipeapp.R
import com.recipeapp.core.platform.BaseFragment
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewmodel.RecipeListViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_list_layout.*


class RecipeListFragment : BaseFragment() {
    override fun layoutId(): Int {
        return R.layout.fragment_recipe_list_layout
    }

    val viewmodel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewmodel::class.java)
    }
    val recipeController = RecipeController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpoxy()
        viewmodel.localRepository =
            RecipeLocalRepository(RecipeDatabase.getDatabase(context!!).recipeDao())
        viewmodel.loadRecipes()
        observeChanges()
        onSavedRecipe()
    }

    fun initEpoxy() {
        rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
                viewmodel.saveRecipe(it)
            }
        }
    }

    fun onSavedRecipe() {
        viewmodel.saveRecipeLiveData.observe(this, Observer {
            when (it) {
                is ViewState.onSuccess -> {
                    Toast.makeText(context, "saved successfully", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
        )
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