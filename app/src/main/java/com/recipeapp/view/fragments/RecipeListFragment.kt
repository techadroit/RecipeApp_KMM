package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.databinding.FragmentRecipeListLayoutBinding
import com.recipeapp.view.adapter.RecipeController
import com.recipeapp.view.viewmodel.RecipeEvent
import com.recipeapp.view.viewmodel.RecipeListViewmodel
import com.recipeapp.view.viewmodel.SideEffect
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipeListFragment : BaseMVIFragment() {

    val viewmodel: RecipeListViewmodel by fragmentViewModel()
    val recipeController = RecipeController()
    lateinit var binding: FragmentRecipeListLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeListLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpoxy()
        viewmodel.loadRecipes()
        observeChanges()
    }

    private fun initEpoxy() {
        recipeController.context = context
        binding.rvList.setController(recipeController)
        recipeController.click = {
            it?.let {
                viewmodel.saveRecipe(it)
            }
        }
    }

    private fun addEndOfListObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.rvList.onScrollBottom().collect {
                if (it) {
                    viewmodel.paginate()
                }
            }
        }
    }

    fun removeEndOfListObserver() {
        binding.rvList.addOnScrollListener(noScrollListener)
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
