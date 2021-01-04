package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.recipeapp.core.platform.BaseFragment
import com.recipeapp.core.platform.ViewState
import com.recipeapp.databinding.FragmentSavedRecipesBinding
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.pojo.RecipeModel
import com.recipeapp.view.viewmodel.FragmentShowSavedRecipesViewmodel

class ShowSavedRecipesFragment : BaseFragment() {

    lateinit var binding: FragmentSavedRecipesBinding

    val viewmodel by lazy {
        ViewModelProviders.of(this).get(FragmentShowSavedRecipesViewmodel::class.java)
    }
    val recipeController = RecipeController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSavedRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        binding.rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
            }
        }
    }

    fun observeChanges() {
        viewmodel.liveData.observe(viewLifecycleOwner, Observer {
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