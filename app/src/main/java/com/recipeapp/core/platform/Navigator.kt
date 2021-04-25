package com.recipeapp.core.platform

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.youtube.player.YouTubePlayer
import com.recipeapp.R
import com.recipeapp.view.fragments.RecipeListFragmentDirections
import com.thefinestartist.ytpa.YouTubePlayerActivity


class Navigator(var navController: NavController, var activity: BaseActivity) {

    fun showRecipeDetail(id: String) {
        addFragment(
            ResId(
                R.id.recipeDetailFragment
            ),
            RecipeListFragmentDirections.navRecipeDetail(id)
        )
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

    fun addFragment(resId: ResId, action: NavDirections? = null) {
        if (action != null) {
            navController.navigate(action)
        } else {
            navController.navigate(resId.resId)
        }
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