package com.recipeapp.usecases

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.recipeapp.UnitTest
import com.recipeapp.core.functional.Either
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.data.repositories.RecipeRepository
import com.recipeapp.domain.usecases.GetRecipeDetailUsecase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetRecipeDetailTest : UnitTest() {

    private val RECIPE_ID = "124995"
    @Mock
    private lateinit var recipeRepository: RecipeRepository
    @Mock
    private lateinit var recipeDetailResponse: RecipeDetailResponse
    private lateinit var usecase: GetRecipeDetailUsecase

    @Before
    fun setup() {
        usecase = GetRecipeDetailUsecase(recipeRepository)
        given { recipeDetailResponse.id }.willReturn(RECIPE_ID)
        given {
            runBlocking {
                recipeRepository.getRecipeDetailForId(
                    RECIPE_ID,
                    false
                )
            }
        }.willReturn(
            Either.Right(recipeDetailResponse)
        )
    }

    @Test
    fun testUsecase() {
        runBlockingTest {
            usecase(GetRecipeDetailUsecase.Param(id = RECIPE_ID,includeNutrition = false),{
                assert(it.isRight)
            })
            verify(recipeRepository).getRecipeDetailForId(RECIPE_ID,false)
            verifyNoMoreInteractions(recipeRepository)
        }
    }

}

