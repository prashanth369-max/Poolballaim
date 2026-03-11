package com.pool.aim.overlay

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pool.aim.state.AppState

class OverlayService : Service() {
    private lateinit var overlayManager: OverlayManager

    override fun onCreate() {
        super.onCreate()
        overlayManager = OverlayManager(this, AppState())
        overlayManager.show()
    }

    override fun onDestroy() {
        overlayManager.hide()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
