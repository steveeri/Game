package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import java.util.*

/**
 * GameObject abstract class, Created by steve on 01/10/16.
 */
abstract class GameObject {
    internal var spriteSheet: Bitmap? = null
    internal var rand = Random()
    internal var x = 0
    internal var y = 0
    internal var dx = 0
    internal var dy = 0
    internal var elapsed: Long = 0
    internal var startTime: Long = 0
    var animation: Animation? = null
    var width = 0
    var height = 0

    internal val rectangle: Rect
        get() = Rect(x, y, x + width, y + height)

    abstract fun update()
    abstract fun draw(canvas: Canvas)

    companion object {
        const val ELAPSED_MS: Long = 100
        const val MS: Long = 1000000
        const val OFFSCREEN_MARGIN = -100
        const val ANIM_DELAY = 10
        const val MAX_GRAVITY = 12
    }
}
