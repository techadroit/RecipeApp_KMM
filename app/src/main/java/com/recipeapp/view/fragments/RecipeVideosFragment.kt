package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import com.haroldadmin.vector.fragmentViewModel
import com.recipeapp.R
import com.recipeapp.core.platform.BaseMVIFragment
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.view.adapter.RecipesVideoController
import com.recipeapp.view.viewmodel.VideoListViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipeVideosFragment : BaseMVIFragment() {
    val viewmodel: VideoListViewmodel by fragmentViewModel()
    val controller = RecipesVideoController()

    override fun layoutId(): Int {
        return R.layout.fragment_recipe_videos_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVideoRecipes()
        viewmodel.getVideoRecipe()
        initController()
    }

    fun initController() {
        rvList.setController(controller)
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
            rvList.onScrollBottom().collect {
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