package com.recipeapp.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A sealed class to represent UI states associated with a resource.
 */
sealed class Resource<out T> : Parcelable{

    abstract override fun hashCode(): Int
    abstract override fun equals(other: Any?): Boolean

    /**
     * A data class to represent the scenario where the resource is available without any errors
     */
    @Parcelize
    data class Success <out T : Parcelable>(val data: T, val isCached: Boolean = false) : Resource<T>() {
        operator fun invoke(): T {
            return data
        }
    }

    @Parcelize
    data class ListData<T : Parcelable>(val data : List<T>,val isCached : Boolean = false) : Resource<T>(){

        var cachedData = mutableListOf<T>()

        init {
            if(isCached){
                cachedData?.addAll(data)
            }
        }

        operator fun invoke() : List<T>{
            return data
        }
    }



    /**
     * A data class to represent the scenario where a resource may or may not be available due to an error
     */
    @Parcelize
    data class Error <out T : Parcelable, out E : Parcelable> (val data: T?, val error: E?) : Resource<T>()

    /**
     * A class to represent the loading state of an object
     */
    @Parcelize
    object Loading : Resource<Nothing>() {
        override fun hashCode(): Int {
            return 2
        }

        override fun equals(other: Any?): Boolean {
            return other is Loading
        }
    }

    @Parcelize
    object Uninitialized : Resource<Nothing>() {
        override fun hashCode(): Int {
            return 1
        }

        override fun equals(other: Any?): Boolean {
            return other is Uninitialized
        }
    }
}

operator fun <T : Parcelable> Resource<T>.invoke(): T? {
    return when {
        this is Resource.Success -> this.data
        this is Resource.Error<T, *> && this.data != null -> this.data
        else -> null
    }
}