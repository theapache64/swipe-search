package com.github.theapache64.swipesearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// 🚀 The journey starts from here
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}