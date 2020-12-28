package com.shared.recipe.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.json.Json


object NetworkClient{

    private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

    private val client by lazy{
        HttpClient(CIO){
            install(JsonFeature) {
                serializer = KotlinxSerializer(nonStrictJson)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    fun getNetworkClient() : HttpClient = client

}