package com.recipeapp.core.platform

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.google.android.youtube.player.YouTubePlayer
import com.recipeapp.R
import com.recipeapp.view.fragments.RecipeDetailFragment
import com.recipeapp.view.fragments.RecipeListFragment
import com.recipeapp.view.fragments.RecipeVideosFragment
import com.recipeapp.view.fragments.ShowSavedRecipesFragment
import com.thefinestartist.ytpa.YouTubePlayerActivity


class Navigator(var navController: NavController,var activity: BaseActivity) {

    fun showRecipeDetail(id: String) {
        addFragment(RecipeDetailFragment(), Bundle().apply {
            putString("id", id)
        }, true)
    }

    fun showRecipeList() {
        addFragment(ResId(R.id.recipeListFragment))
    }

    fun showRecipeVideo() {
        addFragment(ResId(R.id.recipeVideosFragment))
    }

    fun showSavedRecipes() {
        addFragment(ResId(R.id.showSavedRecipesFragment))
    }

    fun addFragment(resId: ResId) {
//        activity.supportFragmentManager.beginTransaction().add(resId.resId, fragment, fragment.tag)
//            .commitAllowingStateLoss()
        navController.navigate(resId.resId)
    }

    fun addFragment(
        fragment: BaseMVIFragment,
        bundle: Bundle? = null,
        addToBackStack: Boolean = false
    ) {
        bundle?.let {
            fragment.arguments = bundle
        }
//        activity.supportFragmentManager.beginTransaction()
//            .add(resId.resId, fragment, fragment.tag).run {
//                if (addToBackStack)
//                    this.addToBackStack(null)
//                this.commitAllowingStateLoss()
//            }
    }

    fun playVideoOnYoutube(youtubeId: Long) {
        val intent = Intent(activity, YouTubePlayerActivity::class.java)
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, youtubeId)
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT)
        activity.startActivity(intent)
    }

}

inline class ResId(val resId: Int)