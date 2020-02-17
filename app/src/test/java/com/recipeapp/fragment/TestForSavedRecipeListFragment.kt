package com.recipeapp.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import com.recipeapp.AndroidTest
import com.recipeapp.view.fragments.RecipeDetailFragment
import com.recipeapp.view.fragments.ShowSavedRecipesFragment
import org.junit.Test
import kotlin.test.assertNotNull


class TestForSavedRecipeListFragment : AndroidTest() {

    @Test
    fun testSavedFragment() {
        val factory = FragmentFactoryImpl()
        val fragment = launchFragmentInContainer<RecipeDetailFragment>(factory = factory)
        assertNotNull(fragment)
    }

    @Test
    fun recreateScenaerio() {
        val fragment = launchFragmentInContainer<RecipeDetailFragment>()
        fragment.recreate()
        assertNotNull(fragment)
    }

    @Test
    fun shouldLoadList() {
        val factory = FragmentFactoryImpl()
        val scenario = launchFragmentInContainer<ShowSavedRecipesFragment>(factory = factory)
        scenario.onFragment {
            it.showLoading()
            assert(it.recipeController.isLoading)
        }

    }

}

class FragmentFactoryImpl : FragmentFactory() {
    val arg = "my argument"
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
//        return when (className) {
//            RecipeDetailFragment.name -> RecipeDetailFragment(arg)
//            else ->
        return super.instantiate(classLoader, className)
//        }
    }
}