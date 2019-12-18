package com.recipeapp

import com.nhaarman.mockitokotlin2.given
import com.recipeapp.core.functional.Either
import com.recipeapp.core.platform.ViewState
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.domain.usecases.GetRecipeDetailUsecase
import com.recipeapp.view.viewmodel.RecipeDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.amshove.kluent.`should equal to`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class RecipeDetailViewModelTest : AndroidTest() {

    private lateinit var viewmodel: RecipeDetailViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    private val RECIPE_ID = "124995"
    @Mock
    private lateinit var usecase: GetRecipeDetailUsecase
    @Mock
    private lateinit var recipeDetailResponse: RecipeDetailResponse

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewmodel = RecipeDetailViewModel()
        viewmodel.usecase = usecase
        given { recipeDetailResponse.id }.willReturn(RECIPE_ID)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }


    @Test
    fun `Fetching recipe details update livedata `() {
        given { runBlocking { usecase.run(GetRecipeDetailUsecase.Param(id = RECIPE_ID)) } }.willReturn(
            Either.Right(
                recipeDetailResponse
            )
        )
        viewmodel.recipeDetailLiveData.observeForever {
            runBlocking(Dispatchers.Main) {
                if(it is ViewState.onSuccess) {
                    val response = it
                    val recipeDetail = response.data as RecipeDetailResponse
                    recipeDetail.id `should equal to` RECIPE_ID
                }
            }
        }

        runBlockingTest { viewmodel.getRecipeDetailForId(RECIPE_ID) }
    }

}