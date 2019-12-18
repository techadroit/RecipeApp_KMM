package com.recipeapp.core.network

import com.google.gson.Gson
import com.recipeapp.BuildConfig
import com.recipeapp.core.network.api_service.ApiService
import com.recipeapp.core.network.api_service.RecipeApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHandler {

    private var BASE_URL: String = "https://api.spoonacular.com/"
    private lateinit var retrofit: Retrofit
    val NUMBER_OF_RETRY = 5L
    val TIMEOUT = 60L

    fun init(cache: Cache? = null) {
        val okHttpClient = createOkHttpClient()
        if (cache != null)
            okHttpClient.cache(cache)
        val gsonConverter = GsonConverterFactory.create(Gson())
        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
        val client = okHttpClient.build()
        /**
         * retrofit instance for rexel api
         */
        retrofit = createRetrofitInstance(BASE_URL, gsonConverter, rxJava2CallAdapterFactory, client)
    }

    private fun createOkHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptor)
        }
        return okHttpClient
    }

    /**
     * create instance of retrofit
     */
    private fun createRetrofitInstance(url: String, gsonConverter: GsonConverterFactory, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(client).build()
    }

    fun getRecipeService(): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }

    fun getRetrofitInstance(): Retrofit{
        return retrofit
    }
}