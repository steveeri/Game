package com.summerland.android.game

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * Score class, Created by steve on 06/10/16.
 */
class Score internal constructor(res: Bitmap, posX: Int, posY: Int, sizeW: Int, sizeH: Int, digits: Int, gap: Int) : GameObject() {

    private val image: Array<Bitmap?>
    private var numDigits = 1
    private var numGap = 0

    init {
        x = posX
        y = posY
        width = sizeW
        height = sizeH
        numGap = gap
        numDigits = digits

        if (numDigits <= 0) numDigits = 1

        spriteSheet = res

        image = arrayOfNulls(10)
        for (i in 0..9) {
            image[i] = Bitmap.createBitmap(spriteSheet!!, i * width, 0, width, height)
        }
    }

    override fun update() {}
    override fun draw(canvas: Canvas) {}
    fun draw(canvas: Canvas, score: Long) {
        draw(canvas, x, y, score)
    }

    fun draw(canvas: Canvas, posX: Int, posY: Int, score: Long) {

        var scoreStr = StringBuilder("" + score)

        if (scoreStr.length > numDigits) {
            scoreStr = StringBuilder(scoreStr.substring(scoreStr.length - numDigits, scoreStr.length))
        } else if (scoreStr.length < numDigits) {
            while (scoreStr.length < numDigits) scoreStr.insert(0, "0")
        }

        try {
            for (i in 0 until numDigits) {
                val xLoc = posX + i * width + i * numGap
                val digit = Integer.parseInt(scoreStr.substring(i, i + 1))
                canvas.drawBitmap(image[digit], xLoc.toFloat(), posY.toFloat(), null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
