package com.summerland.android.game

import android.graphics.Bitmap

/**
 * Animation class, Created by steve on 01/10/16.
 */
class Animation {
    private var frames: Array<Bitmap?> = arrayOfNulls(0)
    private var currentFrame: Int = 0
    private var startTime: Long = 0
    private var delay: Long = 0
    internal var isPlayedOnce = false
        private set

    val image: Bitmap?
        get() = frames[currentFrame]

    internal fun setFrames(frames: Array<Bitmap?>) {
        this.frames = frames
        currentFrame = 0
        startTime = System.nanoTime()
    }

    fun update() {
        val elapsed = (System.nanoTime() - startTime) / MS

        if (elapsed > delay) {
            currentFrame++
            startTime = System.nanoTime()
        }

        if (currentFrame >= frames.size) {
            currentFrame = 0
            isPlayedOnce = true
        }
    }

    internal fun setDelay(d: Long) {
        delay = d
    }

    companion object {
        private val MS: Long = 1000000
    }
}
