package com.recipeapp.core.platform

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.youtube.player.YouTubePlayer
import com.recipeapp.view.fragments.RecipeListFragment
import com.recipeapp.view.fragments.RecipeVideosFragment
import com.recipeapp.view.fragments.ShowSavedRecipesFragment
import com.recipeapp.view.viewmodel.FragmentShowSavedRecipesViewmodel
import com.thefinestartist.ytpa.YouTubePlayerActivity


class Navigator(var activity: FragmentActivity) {

    fun showRecipeList(resId: ResId) {
        addFragment(RecipeListFragment(), resId)
    }

    fun showRecipeVideo(resId: ResId) {
        addFragment(RecipeVideosFragment(), resId)
    }

    fun showSavedRecipes(resId: ResId) {
        addFragment(ShowSavedRecipesFragment(), resId)
    }

    fun addFragment(fragment: BaseFragment, resId: ResId) {
        activity.supportFragmentManager.beginTransaction().add(resId.resId, fragment, fragment.tag)
            .commitAllowingStateLoss()
    }

    fun addFragment(fragment: BaseMVIFragment, resId: ResId) {
        activity.supportFragmentManager.beginTransaction().add(resId.resId, fragment, fragment.tag)
            .commitAllowingStateLoss()
    }

    fun playVideoOnYoutube(youtubeId : Long) {
        val intent = Intent(activity, YouTubePlayerActivity::class.java)
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, youtubeId)
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT)
        activity.startActivity(intent)
    }

}

inline class ResId(val resId: Int)