package com.recipeapp.repository

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.recipeapp.UnitTest
import com.recipeapp.core.functional.Either
import com.recipeapp.core.network.NetworkHandler
import com.recipeapp.core.network.api_service.API_KEY
import com.recipeapp.core.network.api_service.RecipeApi
import com.recipeapp.data.network.response.RandomRecipesResponse
import com.recipeapp.data.network.response.RecipeDetailResponse
import com.recipeapp.data.network.response.RecipeSearchResponse
import com.recipeapp.data.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.HttpException
import retrofit2.Retrofit
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class RecipeRepositoryTest : UnitTest() {

//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)

    @After
    fun after() {
//        Dispatchers.resetMain()
//        testScope.cleanupTestCoroutines()
    }

    @Mock
    private lateinit var retrofit: Retrofit
    @Mock
    private lateinit var recipeApi: RecipeApi
    @Mock
    private lateinit var httpException: HttpException
    @Mock
    private lateinit var response: RandomRecipesResponse
    @Mock
    private lateinit var searchResponse: RecipeSearchResponse
    private lateinit var recipeRepository: RecipeRepository
    @Mock
    private lateinit var recipeDetailResponse: RecipeDetailResponse

    @Before
    fun setup() {
//        Dispatchers.setMain(testDispatcher)
        recipeRepository = RecipeRepository(recipeApi)
        given(response.recipes).willReturn(emptyList())
        given(response.toString()).willReturn("RandomRecipes")
    }

    suspend fun getRandomRecipes() = recipeApi.getRandomRecipes(true, "chicken", 1)

    suspend fun getRecipesFromRemote() = recipeRepository.getRandomRecipe(true, "chicken", 1)

    suspend fun searchRecipeFor() = recipeRepository.searchRecipeFor("chicken", true, 1)

    val recipeId = "124995"
    suspend fun getRecipeDetail() = recipeRepository.getRecipeDetailForId(recipeId, false)

    @Test
    fun runapiTest() {
        runBlocking {
            NetworkHandler.init(null)
            val repository =
                RecipeRepository(NetworkHandler.getRetrofitInstance().create(RecipeApi::class.java))
            val response = repository.getRandomRecipe(true, "chicken", 10)
            val bool = response is Either
            assertTrue(bool)
        }
    }

    @Test
    fun `recipe api should return null or the response`() {
        runBlocking {
            given(
                getRandomRecipes()
            ).willReturn(null)
            val resplonse = getRandomRecipes()
            assertNull(resplonse)
            given(
                getRandomRecipes()
            ).willReturn(response)
            val resplonse1 = getRandomRecipes()
            assertNotNull(resplonse1)
        }
    }

    @Test
    fun `should return recipe for searched keyword`() {

        runBlockingTest {
            given(recipeApi.searchRecipes(true, "chicken", 1, API_KEY)).willReturn(searchResponse)
            val response = searchRecipeFor()
            val bool = response is Either.Right
            assert(bool)
            verify(recipeApi).searchRecipes(true, "chicken", 1, API_KEY)
            verifyNoMoreInteractions(recipeApi)
        }
    }

    @Test
    fun `should return recipe for given id`() {

        runBlocking {
            given { recipeDetailResponse.id }.willReturn(recipeId)
            given { runBlocking { recipeApi.recipeDetail(recipeId, false) } }.willReturn(
                recipeDetailResponse
            )
            val response = recipeRepository.getRecipeDetailForId(recipeId, false)
            response.either({}, { assert(it.id.equals(recipeId)) })
            verify(recipeApi).recipeDetail(recipeId,false)
            verifyNoMoreInteractions(recipeApi)
        }
    }
}
