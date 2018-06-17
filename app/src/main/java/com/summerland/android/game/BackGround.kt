package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * BackGround class, Created by steve on 01/10/16.
 */
class BackGround internal constructor(private val image: Bitmap, private val dx: Int) {
    private var x: Int = 0
    private val y: Int = 0

    fun update() {
        x += dx
        if (x < -BackGround.WIDTH) x = 0
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
        if (x < 0) canvas.drawBitmap(image, (x + BackGround.WIDTH).toFloat(), y.toFloat(), null)
    }

    companion object {

        internal val WIDTH = 856
        internal val HEIGHT = 480
    }

    //public void setVector(int dx){ this.dx = dx; }
}

