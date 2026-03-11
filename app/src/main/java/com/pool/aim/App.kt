package com.pool.aim

import android.app.Application
import com.pool.aim.geometry.GeometryBridge

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        GeometryBridge.load()
    }
}
