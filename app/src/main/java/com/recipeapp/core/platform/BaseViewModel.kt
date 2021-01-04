/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.recipeapp.core.platform

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.haroldadmin.vector.SavedStateVectorViewModel
import com.haroldadmin.vector.VectorState
import com.recipeapp.core.exception.Failure
import com.shared.recipe.repository.ApiFailure
import kotlinx.parcelize.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

    fun showLoading(live: MutableLiveData<ViewState>) {
        live.postValue(ViewState.onLoading(true))
    }

    fun hideLoading(live: MutableLiveData<ViewState>) {
        live.postValue(ViewState.onLoading(false))
    }
}

@Parcelize
object Empty : Parcelable

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseMVIViewmodel<T : RecipeState>(
    initialState: T,
    savedStateHandle: SavedStateHandle
) : SavedStateVectorViewModel<T>(initialState = initialState,savedStateHandle = savedStateHandle) {


    var failure: MutableLiveData<Failure> = MutableLiveData()
    val viewModelScope = CoroutineScope(Dispatchers.Default + Job())

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
    protected fun handleFailure(failure: ApiFailure) {
//        this.failure.value = failure
    }


}

@Parcelize
open class RecipeState : VectorState,Parcelable