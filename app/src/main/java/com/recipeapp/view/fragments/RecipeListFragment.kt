package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.R
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.data.datasource.RecipeDatabase
import com.recipeapp.data.repositories.RecipeLocalRepository
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.viewmodel.RecipeEvent
import com.recipeapp.view.viewmodel.RecipeListViewmodel
import com.recipeapp.view.viewmodel.SideEffect
import kotlinx.android.synthetic.main.fragment_recipe_list_layout.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
        viewmodel.loadRecipes()
        observeChanges()
    }

    private fun initEpoxy() {
        recipeController.context = context
        rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
                viewmodel.saveRecipe(it)
            }
        }
    }

    private fun addEndOfListObserver() {
        viewScope.launch {
            rvList.onScrollBottom().collect {
                if (it) {
                    viewmodel.paginate()
                }
            }
        }
    }

    fun removeEndOfListObserver() {
        rvList.addOnScrollListener(noScrollListener)
    }

    private fun observeChanges() {
        renderState(viewmodel) { state ->
            checkToAddPagination(state.event)
            recipeController.setState(state)
            state.sideEffect?.data?.let { onSideEffect(it) }
        }
    }

    private fun onSideEffect(sideEffect: SideEffect) {
        when (sideEffect) {
            is SideEffect.OnSavedRecipe -> Toast.makeText(
                context,
                " Recipe saved successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkToAddPagination(event: RecipeEvent) {
        when (event) {
            is RecipeEvent.OnRecipeInitialLoad,
            is RecipeEvent.OnRecipePaginate
            -> addEndOfListObserver()
            else -> removeEndOfListObserver()
        }
    }
}

fun RecyclerView.onScrollBottom(): Flow<Boolean> = callbackFlow<Boolean> {
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                channel.offer(true)
            }
        }
    }

    this@onScrollBottom.addOnScrollListener(scrollListener)
    awaitClose { this@onScrollBottom.removeOnScrollListener(scrollListener) }
}

val noScrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }
}
