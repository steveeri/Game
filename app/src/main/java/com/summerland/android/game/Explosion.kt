package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Explosion class, Created by steve on 06/10/16.
 */
class Explosion internal constructor(res: Bitmap, posX: Int, posY: Int, sizeW: Int, sizeH: Int, columns: Int, numFrames: Int) : GameObject() {

    init {
        x = posX
        y = posY
        width = sizeW
        height = sizeH

        spriteSheet = res

        val image = arrayOfNulls<Bitmap>(numFrames)
        var col = 0
        var row = 0
        while (col < numFrames) {

            // Calculate grid offsets to load images from spriteSheet.
            if (col != 0 && col % columns == 0) row++
            val xOffset = (col - row * columns) * width
            val yOffset = row * height
            image[col] = Bitmap.createBitmap(spriteSheet!!, xOffset, yOffset, width, height)
            col++
        }

        // load images to animation handler.
        animation = Animation()
        animation!!.setFrames(image)
        animation!!.setDelay(GameObject.ANIM_DELAY.toLong())
        startTime = System.nanoTime()
    }

    override fun update() {
        if (!animation!!.isPlayedOnce) {
            animation!!.update()
        }
    }

    override fun draw(canvas: Canvas) {
        if (!animation!!.isPlayedOnce) {
            canvas.drawBitmap(animation!!.image, x.toFloat(), y.toFloat(), null)
        }
    }
}
