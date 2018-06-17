package com.summerland.android.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Smoke class, Created by steve on 01/10/16.
 */
class Smoke internal constructor(x: Int, y: Int) : GameObject() {

    init {
        this.x = x
        this.y = y
    }

    override fun update() {
        x -= 10
    }

    override fun draw(canvas: Canvas) {
        draw(canvas, true)
    }

    fun draw(canvas: Canvas, goingUp: Boolean) {

        var size = DIA
        val paint = Paint()
        paint.color = Color.GRAY
        paint.style = Paint.Style.FILL

        if (goingUp) {
            paint.color = Color.DKGRAY
            size = UP_DIA
        }

        canvas.drawCircle((x - size).toFloat(), (y - size).toFloat(), size.toFloat(), paint)
        canvas.drawCircle((x - size + 2).toFloat(), (y - size - 2).toFloat(), size.toFloat(), paint)
        paint.color = Color.GRAY
        canvas.drawCircle((x - size + 4).toFloat(), (y - size - 2).toFloat(), size.toFloat(), paint)
    }

    companion object {
        private val DIA = 4
        private val UP_DIA = 5
    }
}
