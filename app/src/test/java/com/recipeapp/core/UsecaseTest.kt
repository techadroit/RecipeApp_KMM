package com.recipeapp.core

import com.recipeapp.AndroidTest
import com.recipeapp.core.exception.Failure
import com.recipeapp.core.functional.Either
import com.recipeapp.core.usecase.UseCase
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Test

class UsecaseTest  : AndroidTest(){

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>() {
        override suspend fun run(params: MyParams) = Either.Right(MyType(TYPE_TEST))
    }

    private val useCase = MyUseCase()

    @Test
    fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        result shouldEqual Either.Right(MyType(TYPE_TEST))
    }

    @Test fun `should return correct data when executing use case`() {
        var result: Either<Failure, MyType>? = null

        val params = MyParams("TestParam")
        val onResult = { myResult: Either<Failure, MyType> -> result = myResult }

        runBlocking { useCase(params, onResult) }

        result shouldEqual Either.Right(MyType(TYPE_TEST))
    }
}