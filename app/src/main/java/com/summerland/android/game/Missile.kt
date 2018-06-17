package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Missile class, Created by steve on 01/10/16.
 */
class Missile internal constructor(res: Bitmap, posX: Int, posY: Int, sizeW: Int, sizeH: Int, score: Int, numFrames: Int) : GameObject() {

    private var speed: Int = 0

    init {
        x = posX
        y = posY
        width = sizeW
        height = sizeH

        // Speed increases with score making hits more likely. Capped at 40.
        speed = 9 + (rand.nextDouble() * score / 30).toInt()
        if (speed > 40) speed = 40

        spriteSheet = res
        val image = arrayOfNulls<Bitmap>(numFrames)
        for (i in 0 until numFrames) {
            image[i] = Bitmap.createBitmap(spriteSheet!!, 0, i * height, width, height)
        }

        animation = Animation()
        animation!!.setFrames(image)
        animation!!.setDelay((GameObject.ANIM_DELAY - speed).toLong())
        startTime = System.nanoTime()
    }

    override fun update() {
        x -= speed
        animation!!.update()
    }

    override fun draw(canvas: Canvas) {
        try {
            canvas.drawBitmap(animation!!.image, x.toFloat(), y.toFloat(), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    //public int getSpeed() { return speed; }
    //public void setSpeed(int speed) { this.speed = speed; }
}
