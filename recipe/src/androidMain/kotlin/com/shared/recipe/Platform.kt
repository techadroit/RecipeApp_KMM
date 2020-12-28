package com.shared.recipe


import android.content.Context

lateinit var appContext: Context

actual fun getApplicationFilesDirectoryPath(): String =
    appContext.filesDir.absolutePath

