package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point

/**
 * Player class, Created by steve on 01/10/16.
 */
class Player internal constructor(res: Bitmap, posX: Int, posY: Int, sizeW: Int, sizeH: Int, numFrames: Int) : GameObject() {
    internal var score = 0
        private set
    internal var isGoingUp = false
    private var playing = false
    internal var isCollided = false
        private set

    internal var isPlaying: Boolean
        get() = playing
        set(playing) {
            if (playing) score = 0
            this.playing = playing
        }

    init {
        x = posX
        y = posY
        dy = 0

        spriteSheet = res
        width = sizeW
        height = sizeH

        val image = arrayOfNulls<Bitmap>(numFrames)

        for (i in 0 until numFrames) {
            image[i] = Bitmap.createBitmap(spriteSheet!!, i * width, 0, width, height)
        }

        animation = Animation()
        animation!!.setFrames(image)
        animation!!.setDelay(GameObject.ANIM_DELAY.toLong())
        startTime = System.nanoTime()
    }

    override fun update() {
        elapsed = System.nanoTime() - startTime / GameObject.MS
        if (elapsed > GameObject.ELAPSED_MS) {
            score++
            startTime = System.nanoTime()
            // System.out.println("Players Score: " +  score);
        }

        animation!!.update()

        if (isGoingUp)
            dy -= 1
        else
            dy += 1
        if (dy > 14) dy = GameObject.MAX_GRAVITY
        if (dy < -14) dy = -GameObject.MAX_GRAVITY

        y += dy * 2
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(animation!!.image, x.toFloat(), y.toFloat(), null)
    }

    internal fun reset(point: Point) {
        x = point.x
        y = point.y
        //score = 0;
        dy = 0
        isGoingUp = false
        playing = false
        isCollided = false
    }

    internal fun setCollided() {
        this.isCollided = true
    }
}
