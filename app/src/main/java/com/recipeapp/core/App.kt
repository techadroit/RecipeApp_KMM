package com.recipeapp.core

import android.app.Application
import com.haroldadmin.vector.Vector
import com.recipeapp.core.network.NetworkHandler
import com.shared.recipe.appContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        NetworkHandler.init(null)
        Vector.enableLogging = true
    }
}