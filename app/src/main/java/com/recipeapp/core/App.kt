package com.recipeapp.core

import android.app.Application
import com.haroldadmin.vector.Vector
import com.shared.recipe.appContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Vector.enableLogging = true
    }
}