package com.summerland.android.game

import android.graphics.Canvas
import android.view.SurfaceHolder

/**
 * MainThread class, Created by steve on 30/09/16.
 */
class MainThread internal constructor(private val surfaceHolder: SurfaceHolder, private val gamePanel: GamePanel) : Thread() {

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        //long totalTime = 0, frameCount = 0;
        val targetTime = (1000 / FPS).toLong()

        while (running) {
            startTime = System.nanoTime()
            var canvas: Canvas? = null

            // try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.gamePanel.update()
                    this.gamePanel.draw(canvas)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null)
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime >= 0) {
                try {
                    sleep(waitTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            //totalTime += System.nanoTime() - startTime;
            //frameCount++;
            //if (frameCount == FPS) {
            //    averageFPS = 1000/((totalTime/frameCount)/1000000);
            //    totalTime = 0;
            //    frameCount = 0;
            //    System.out.println("Average FPS: " + averageFPS);
            //}
        }
    }

    internal fun setRunning(run: Boolean) {
        running = run
    }

    companion object {
        private const val FPS = 30
        private var running: Boolean = false
    }
}
