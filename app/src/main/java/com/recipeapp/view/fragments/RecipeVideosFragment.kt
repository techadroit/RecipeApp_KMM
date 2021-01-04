package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.databinding.FragmentRecipeVideosListBinding
import com.recipeapp.view.adapter.RecipesVideoController
import com.recipeapp.view.viewmodel.VideoListViewmodel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipeVideosFragment : BaseMVIFragment() {
    val viewmodel: VideoListViewmodel by fragmentViewModel()
    val controller = RecipesVideoController()

    lateinit var binding: FragmentRecipeVideosListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeVideosListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVideoRecipes()
        viewmodel.getVideoRecipe()
        initController()
    }

    fun initController() {
        binding.rvList.setController(controller)
        controller.click = {
            when (it) {
                is OnClickEvent.PlayVideo -> {
                    val youtubeId = it.id
                    navigator?.playVideoOnYoutube(youtubeId)
                }
                else -> {
                }
            }
        }

        viewScope.launch {
            binding.rvList.onScrollBottom().collect {
                if (it) {
                    viewmodel.loadMoreRecipes()
                }
            }
        }
    }

    fun observeVideoRecipes() {

        renderState(viewmodel) { state ->
            controller.setState(state)
        }
    }
}