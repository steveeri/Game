package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Border class, Created by steve on 05/10/16.
 */
class Border internal constructor(res: Bitmap, posX: Int, posY: Int, sizeW: Int, sizeH: Int) : GameObject() {

    private val image: Bitmap

    init {
        x = posX
        y = posY
        width = sizeW
        height = sizeH
        spriteSheet = res

        dx = GamePanel.MOVE_SPEED
        image = Bitmap.createBitmap(res, 0, 0, width, height)
    }

    override fun update() {
        x += dx
    }

    override fun draw(canvas: Canvas) {
        try {
            canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
