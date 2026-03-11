package com.pool.aim.state

import com.pool.aim.calibration.TableBounds
import com.pool.aim.geometry.Vector2

data class AppState(
    var redMarker: Vector2 = Vector2(600f, 600f),
    var whiteMarker: Vector2 = Vector2(900f, 600f),
    var tableBounds: TableBounds = TableBounds(100f, 1800f, 200f, 1000f),
    var reflectionCount: Int = 2,
    var showAimLines: Boolean = true,
    var showPockets: Boolean = true,
    var markerLocked: Boolean = false
)
