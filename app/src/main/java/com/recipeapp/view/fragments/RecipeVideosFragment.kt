package com.recipeapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.recipeapp.R
import com.recipeapp.core.platform.BaseFragment
import com.recipeapp.core.platform.OnClickEvent
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.network.response.Video
import com.recipeapp.view.adapter.RecipesVideoController
import com.recipeapp.view.viewmodel.VideoListViewmodel
import kotlinx.android.synthetic.main.fragment_recipe_detail.*

class RecipeVideosFragment : BaseFragment() {

    val viewmodel by lazy {
        ViewModelProviders.of(this).get(VideoListViewmodel::class.java)
    }

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
    }

    fun observeVideoRecipes() {
        viewmodel.videosListLiveData.observe(this@RecipeVideosFragment, Observer {
            when (it) {
                is ViewState.onSuccess -> {
                    val list = it.data as List<Video>
                    controller.list.addAll(list)
                    controller.requestModelBuild()
                }
                is ViewState.onLoading -> {
                    controller.isLoading = it.loading
                    controller.requestModelBuild()
                }
                is ViewState.onError -> {
                }
            }
        })
    }
}