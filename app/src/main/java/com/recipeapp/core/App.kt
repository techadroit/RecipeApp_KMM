package com.recipeapp.core

import android.app.Application
import com.recipeapp.core.network.NetworkHandler

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkHandler.init(null)
    }
}