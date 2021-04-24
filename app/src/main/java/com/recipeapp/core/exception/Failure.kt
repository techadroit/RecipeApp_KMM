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
package com.recipeapp.core.exception

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
abstract class Failure : Exception(), Parcelable {

}

@Parcelize
object NetworkConnection : Failure()

@Parcelize
object ServerError : Failure()

@Parcelize
object Unauthorized : Failure()

@Parcelize
object UnknonwnError : Failure()

/** * Extend this class for feature specific failures.*/
abstract class FeatureFailure : Failure()

@Parcelize
object NoSavedRecipe : FeatureFailure()

