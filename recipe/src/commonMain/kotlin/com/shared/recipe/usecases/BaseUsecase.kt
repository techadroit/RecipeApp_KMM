package com.shared.recipe.usecases

import com.shared.recipe.repository.ApiFailure
import com.shared.recipe.resource.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class BaseUsecase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<ApiFailure, Type>

    operator fun invoke(params: Params, onResult: (Either<ApiFailure, Type>) -> Unit = {}) {
        CoroutineScope(Dispatchers.Default).async {
            val job = run(params)
            CoroutineScope(Dispatchers.Main).launch {
                onResult(job)
            }
        }
    }

    class None
}
